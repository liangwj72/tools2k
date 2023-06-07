package com.liangwj.tools2k.utils.threadPool;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.util.Assert;

import com.liangwj.tools2k.beans.system.OsInfo;
import com.liangwj.tools2k.utils.other.DateUtil;
import com.liangwj.tools2k.utils.other.LogUtil;
import com.liangwj.tools2k.utils.other.MBeanUtils;
import com.liangwj.tools2k.utils.other.QueueLinkedList;

import jakarta.annotation.PreDestroy;

/**
 * 一个线程池
 *
 * @author rock
 *
 */
public abstract class BaseThreadPool {
	private static final int POOL_SIZE_TIME = 1;

	class MyTask implements Runnable {
		private final IMyTask task;

		public MyTask(IMyTask task) {
			this.task = task;
		}

		@Override
		public void run() {
			if (BaseThreadPool.this.quit) {
				return;
			}

			long now = System.nanoTime();

			if (!BaseThreadPool.this.isOverQueueLimit()) {
				// 如果未超限，就执行
				try {
					this.task.run();
				} catch (RuntimeException e) {
					LogUtil.traceError(log, e);
				}
			} else {
				// 如果超限，就记录
				BaseThreadPool.this.onGiveUp();
			}
			// 记录任务总数和执行总时间
			long cost = System.nanoTime() - now;
			lastTime = System.currentTimeMillis();
			afterMainTask(task, cost);
		}
	}

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BaseThreadPool.class);

	public static void shutdownExecutorService(String name, ExecutorService executorService) {
		if (executorService == null) {
			return;
		}

		executorService.shutdown();
		try {
			// 5分钟如果不能停止就强行停止
			if (!executorService.awaitTermination(10, TimeUnit.MINUTES)) {
				executorService.shutdownNow();
				if (!executorService.awaitTermination(1, TimeUnit.MINUTES)) {
					log.error(name + " shutdown 失败");
				}
			}
		} catch (InterruptedException e) {
			LogUtil.traceError(log, e);
			executorService.shutdownNow();
		}
		log.info("成功停止线程池: “{}” ", name);
	}

	private long lastTime = 0;

	/**
	 * 在排队中的任务数量
	 */
	private final AtomicLong unfinishCount = new AtomicLong();

	/** 队列超限时放弃的任务数量 */
	private final AtomicLong giveUp = new AtomicLong();

	/** 任务总数 */
	private final AtomicLong counterTotal = new AtomicLong();

	/**
	 * 任务总数
	 */
	private final AtomicLong finishedCount = new AtomicLong();

	/**
	 * 总花费时间
	 */
	private final AtomicLong finishedCost = new AtomicLong();

	private transient long maxCost = 0;

	private transient long maxUnfinish = 0;

	private transient long minCost = Long.MAX_VALUE;

	/** 队列长度限制，避免未完成的任务过多 */
	private long queueLimit = 2000;

	/** 主线程池，处理游戏的各类事件、聊天、发送系统信息等 */
	private final ExecutorService executorService;

	/** 维护任务set的锁 */
	private final Lock taskSetLock = new ReentrantLock();

	/** 任务集合，用于防治重复提交任务 */
	private final Set<String> taskUniqueIdSet;

	/** 记录最近的50条放弃的时间，10分钟内只记录一次 */
	private final QueueLinkedList<String> giveUpTimeList = new QueueLinkedList<>(50);

	/** 最后记录的放弃的时间 */
	private long lastGiveUpTime;

	private boolean quit = false;

	private final Lock onGiveupLock = new ReentrantLock();

	public BaseThreadPool() {
		log.info("初始化线程池:{} {}, 线程数量:{}",
				this.getClass().getSimpleName(),
				this.getName(), this.getPoolSize());

		this.executorService = Executors.newFixedThreadPool(this.getPoolSize());
		this.taskUniqueIdSet = new HashSet<>(this.getUniqueIdSetInitSize());
	}

	/**
	 * 增加新的任务,不做排重
	 *
	 * @param task
	 *            要增加的任务
	 */
	public boolean addNewTask(Runnable task) {
		return this.addNewTask(new IMyTask() {

			@Override
			public void run() {
				if (!quit) {
					task.run();
				}
			}

			@Override
			public String getUniqueId() {
				return null;
			}
		});
	}

	/**
	 * 增加新的任务
	 *
	 * @param task
	 *            要增加的任务
	 * @return 如果任务没有重复，可以增加成功就返回真
	 */
	public boolean addNewTask(IMyTask task) {
		if (this.quit) {
			// 如果已经shutdown了，就直接返回
			return false;
		}

		Assert.notNull(task, "task 不能为空");

		this.counterTotal.incrementAndGet(); // 任务总数+1

		if (this.isOverQueueLimit()) {
			// 如果未完成任务超限了，直接就不加新任务了
			this.onGiveUp();
			return false;
		}

		String key = task.getUniqueId();
		if (key != null) {
			// 如果该任务有唯一性校验
			this.taskSetLock.lock();
			try {
				if (this.taskUniqueIdSet.contains(key)) {
					if (log.isDebugEnabled()) {
						log.debug(String.format("在线程池 %s 增加任务 %s, 但该任务已经存在", this.getName(), task.toString()));
					}
					// 如果已经存在在任务，就直接返回
					return false;
				} else {
					// 如果没有该任务，就添加到set中
					this.taskUniqueIdSet.add(key);
				}
			} finally {
				this.taskSetLock.unlock();
			}
		}

		long unfinish = unfinishCount.incrementAndGet();// 加入任务时，计数器+1
		if (unfinish > this.maxUnfinish) {
			// 记录一下最大的未完成任务数量
			this.maxUnfinish = unfinish;
		}

		this.executorService.execute(new MyTask(task));

		if (log.isDebugEnabled()) {
			log.debug(String.format("在线程池 %s 增加任务 %s，待执行任务数量 %d", this.getName(), task.toString(),
					this.unfinishCount.get()));
		}
		return true;
	}

	@ManagedAttribute(description = "任务执行的平均时间(微秒)")
	public long getFinishedTaskAvgCost() {
		long count = this.getFinishedTaskCount();
		if (count == 0) {
			return 0;
		} else {
			return this.finishedCost.get() / count / 1000L;
		}
	}

	@ManagedAttribute(description = "已执行完的任务的数量")
	public long getFinishedTaskCount() {
		return this.finishedCount.get();
	}

	@ManagedAttribute(description = "队列超限时放弃的数量")
	public long getGiveUp() {
		return giveUp.get();
	}

	@ManagedAttribute(description = "放弃率")
	public String getGiveUpRate() {
		long total = this.counterTotal.get();
		if (total > 0) {
			return (this.giveUp.get() * 100 / total) + "%";
		}
		return null;
	}

	@ManagedAttribute(description = "队列超限时放弃的时间")
	public List<String> getGiveUpTimeList() {
		return giveUpTimeList;
	}

	@ManagedAttribute(description = "任务最后执行的时间")
	public String getLastTime() {
		if (this.lastTime > 0) {
			return DateUtil.dateFormat(new Date(lastTime));
		} else {
			return null;
		}
	}

	@ManagedAttribute(description = "任务执行的最长时间(微秒)")
	public long getMaxCost() {
		return maxCost / 1000L;
	}

	@ManagedAttribute(description = "任务执行的最短时间(微秒)")
	public long getMinCost() {
		if (this.getFinishedTaskCount() == 0) {
			return 0;
		}
		return minCost / 1000L;
	}

	@ManagedAttribute(description = "任务排队队列最大值")
	public long getMaxUnfinish() {
		return maxUnfinish;
	}

	@ManagedAttribute(description = "线程池数量")
	public int getPoolNum() {
		// 仅jmx用
		return this.getPoolSize();
	}

	@ManagedAttribute(description = "线程池名字")
	public String getPoolName() {
		// 仅jmx用
		return this.getName();
	}

	@ManagedAttribute(description = "队列长度限制，超限就放弃")
	public long getQueueLimit() {
		return queueLimit;
	}

	/**
	 * 获得主线程池正在执行的任务的数量
	 *
	 * @return
	 */
	@ManagedAttribute(description = "当前未执行完的任务的数量")
	public long getUnFinishTaskCount() {
		return unfinishCount.get();
	}

	@ManagedAttribute(description = "任务排重集合的大小")
	public int getUniqueIdSetSize() {
		return this.taskUniqueIdSet.size();
	}

	@ManagedOperation(description = "重置计数器")
	public void reset() {
		this.maxCost = 0;
		this.maxUnfinish = 0;
		this.minCost = Long.MAX_VALUE;
		this.finishedCount.set(0);
		this.finishedCost.set(0);
		this.giveUp.set(0);
	}

	@ManagedAttribute()
	public void setQueueLimit(long queueLimit) {
		if (queueLimit > 0) {
			this.queueLimit = queueLimit;
		}
	}

	@PreDestroy
	public void shutdown() {
		this.quit = true;
		log.info("开始停止 “{}”， 剩余任务:{} ", this.getName(), this.unfinishCount.get());
		shutdownExecutorService(this.getName(), this.executorService);
	}

	/**
	 * 任务执行完成后，将任务的id从set中移除
	 *
	 * @param task
	 * @param cost
	 */
	private void afterMainTask(IMyTask task, long cost) {
		String key = task.getUniqueId();
		if (key != null) {
			this.taskSetLock.lock();
			try {
				this.taskUniqueIdSet.remove(key);
			} finally {
				this.taskSetLock.unlock();
			}
		}

		unfinishCount.decrementAndGet();// 任务执行完成时，计数器-1

		// 记录时间开销
		finishedCost.addAndGet(cost);
		finishedCount.incrementAndGet();

		if (this.maxCost < cost) {
			this.maxCost = cost;
		}
		if (this.minCost > cost) {
			this.minCost = cost;
		}
	}

	/** 未完成任务数量是否超限 */
	private boolean isOverQueueLimit() {
		return this.getUnFinishTaskCount() > this.getQueueLimit();
	}

	/** 当放弃的时候，记录时间 */
	private void onGiveUp() {
		this.giveUp.incrementAndGet();

		this.onGiveupLock.lock();
		try {

			long now = System.currentTimeMillis();
			if (now - this.lastGiveUpTime > TimeUnit.MINUTES.toMillis(10)) {
				// 如果距离上次放弃的时间超过了10分钟，就记录一下
				this.lastGiveUpTime = now;

				// 将时间点也记录下来，方便查看
				String dateStr = DateUtil.dateFormat(new Date(now));
				this.giveUpTimeList.add(dateStr);
			}
		} finally {
			this.onGiveupLock.unlock();
		}
	}

	/**
	 * 线程池的名字，用于显示在日志
	 *
	 * @return
	 */
	protected abstract String getName();

	/**
	 * 线程池的大小
	 *
	 * @return
	 */
	protected int getPoolSize() {
		// 线程数量=CPU数量 * 2
		OsInfo os = MBeanUtils.getOsInfo();
		return os.getAvailableProcessors() * POOL_SIZE_TIME;
	};

	/**
	 * 唯一任务ID排重HashSet大小的初始值
	 *
	 * @return
	 */
	protected abstract int getUniqueIdSetInitSize();

}
