package com.liangwj.tools2k.utils.threadPool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;

import com.liangwj.tools2k.beans.system.OsInfo;
import com.liangwj.tools2k.utils.other.MBeanUtils;

import jakarta.annotation.PreDestroy;

/**
 * 
 * <pre>
 *  ScheduledExecutorService 基类
 * </pre>
 *
 */
public abstract class BaseScheduledExecutor {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BaseScheduledExecutor.class);

	/** 任务执行线程池 */
	private final ScheduledExecutorService execThreadPool;

	private final int poolSize;

	/** 未完成任务计数器，添加任务就+1，任务完成就-1 */
	private final AtomicInteger unfinishTaskCount = new AtomicInteger();

	private final AtomicInteger totalCount = new AtomicInteger();
	private final AtomicInteger errorCount = new AtomicInteger();

	private long costMax = -1;
	private long costMin = 1000;
	private long costTotal;
	private final Lock counterLock = new ReentrantLock();

	private boolean quit;

	private IRunnable onAllDoneCallBack;

	public BaseScheduledExecutor() {
		this.poolSize = this.initPoolSize();
		this.execThreadPool = Executors.newScheduledThreadPool(poolSize);
	}

	/** 线程池数量 默认就是CPU数量 */
	protected int initPoolSize() {
		// 线程数量=CPU数量
		OsInfo os = MBeanUtils.getOsInfo();
		return os.getAvailableProcessors();
	}

	protected abstract String getName();

	/** 加入一个延时任务 */
	public void schedule(IRunnable task, long delay, TimeUnit timeunit) {
		if (this.quit) {
			return;
		}

		unfinishTaskCount.incrementAndGet(); // 计算器+1
		this.execThreadPool.schedule(() -> {

			long now = System.currentTimeMillis();
			try {
				task.run();
			} catch (Throwable e) {
				errorCount.incrementAndGet();
			}

			int unfinish = unfinishTaskCount.decrementAndGet(); // 执行完成后，计算器-1
			if (unfinish == 0) {
				// 如果所有认为都完成了，就看看是否需要回调
				this.onAllDone();
			}

			long cost = System.currentTimeMillis() - now;
			this.addCostTime(cost);

		}, delay, timeunit);
	}

	/** 执行定时任务 */
	public void scheduleAtFixedRate(Runnable command,
			long initialDelay,
			long period,
			TimeUnit timeunit) {
		this.execThreadPool.scheduleAtFixedRate(command, initialDelay, period, timeunit);
	}

	/** 如果有callback 就调用 */
	private void onAllDone() {
		if (this.onAllDoneCallBack != null) {
			// callback只调用一次，所有调用后要清空
			IRunnable callback = this.onAllDoneCallBack;
			this.onAllDoneCallBack = null;

			// 回调方法需要异步执行
			this.schedule(callback, 0);
		}
	}

	/** 加入一个延时任务, 时间单位是毫秒 */
	public void schedule(IRunnable task, long delay) {
		this.schedule(task, delay, TimeUnit.MILLISECONDS);
	}

	@ManagedAttribute(description = "已完成任务总数")
	public int getTotalCount() {
		return this.totalCount.get();
	}

	@ManagedAttribute(description = "线程数")
	public int getPoolSize() {
		return poolSize;
	}

	@ManagedAttribute(description = "出错次数")
	public int getErrorCount() {
		return this.errorCount.get();
	}

	@ManagedAttribute(description = "待执行任务数")
	public int getUnfinishTaskCount() {
		return this.unfinishTaskCount.get();
	}

	@ManagedOperation(description = "重置计数器")
	public void resetCounter() {
		this.counterLock.lock();
		try {
			this.costMax = 0;
			this.costMin = 10000;
			this.costTotal = 0;
		} finally {
			this.counterLock.unlock();
		}
		this.errorCount.set(0);
		this.totalCount.set(0);
	}

	@ManagedAttribute(description = "任务平均耗时")
	public long getCostAvg() {
		int total = this.totalCount.get();
		if (total > 0) {
			return this.costTotal / total;
		} else {
			return 0;
		}
	}

	@ManagedAttribute(description = "任务最大耗时")
	public long getCostMax() {
		return costMax;
	}

	@ManagedAttribute(description = "任务最小耗时")
	public long getCostMin() {
		return costMin;
	}

	/**
	 * 增加时间消耗
	 */
	private void addCostTime(long cost) {
		totalCount.incrementAndGet();

		if (cost > 2000) {
			log.warn("{} 出现时间超过2000毫秒的任务", this.getName());
		}

		this.counterLock.lock();
		try {
			this.costTotal += cost;
			if (cost > this.costMax) {
				this.costMax = cost;
			}
			if (cost < this.costMin) {
				this.costMin = cost;
			}
		} finally {
			this.counterLock.unlock();
		}
	}

	/** 设置所有任务完成时的回调 */
	public void onAllDone(IRunnable onAllDoneCallBack) {
		this.onAllDoneCallBack = onAllDoneCallBack;
	}

	@PreDestroy
	protected void onShutdown() {
		this.quit = true;
		BaseThreadPool.shutdownExecutorService(getName(), this.execThreadPool);
	}
}
