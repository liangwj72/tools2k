package com.liangwj.tools2k.apiServer.security;

import java.lang.management.ManagementFactory;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.apiServer.beans.manager.PayloadCounter;
import com.liangwj.tools2k.apiServer.beans.manager.RuntimeInfoBean;
import com.liangwj.tools2k.utils.other.MBeanUtils;
import com.liangwj.tools2k.utils.other.OsUtil;
import com.liangwj.tools2k.utils.other.QueueLinkedList;
import com.liangwj.tools2k.utils.threadPool.BaseThreadPool;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

/**
 * <pre>
 * 用于统计的计数器服务
 * 
 * </pre>
 * 
 * @author rock
 * 
 */
@Service
public class CounterService {
	/** 记录的历史长度 */
	public static final int HISTROY_LEN = 60;

	/** 时间间隔(秒） */
	public static final int TIME_INTERVAL = 10;

	public class SnapshotBean {

		@AComment("启动时间")
		private final long uptime = MBeanUtils.getVmInfo().getUptime();

		private final long processCpuTime = MBeanUtils.getOsInfo().getProcessCpuTime();

		@AComment("ws请求次数")
		private long wsUpCount;

		@AComment("ws请求带宽")
		private long wsUpPayload;

		@AComment("ws发送消息次数")
		private long wsDownCount;

		@AComment("ws发送消息带宽")
		private long wsDownPayload;

		@AComment("http请求次数")
		private long actionCount;

		@AComment("发包次数")
		private long sendPacketCount;

		@AComment("发包带宽")
		private long sendPacketPayload;

		public void reset() {
			this.actionCount = 0;
			this.wsDownCount = 0;
			this.wsDownPayload = 0;
			this.wsUpCount = 0;
			this.wsUpPayload = 0;
			this.sendPacketCount = 0;
			this.sendPacketPayload = 0;
		}

	}

	/** 运行时的历史信息 */
	private final QueueLinkedList<RuntimeInfoBean> history = new QueueLinkedList<>(HISTROY_LEN);

	/** 动态请求 */
	private final PayloadCounter actionCounter = new PayloadCounter();

	/** WS上行使用情况 */
	private final PayloadCounter wsUpCounter = new PayloadCounter();

	/** WS下行使用情况 */
	private final PayloadCounter wsDownCounter = new PayloadCounter(); // 下行使用情况

	/** 网络发包情况 */
	private final PayloadCounter sendPacketCounter = new PayloadCounter();

	private boolean hasSendPacketData = false;

	/** 定时获取运行状态的调度器 */
	private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

	/** 上一个时间点的运行状态（累计值） */
	private SnapshotBean lastSnapShot = new SnapshotBean();

	/** 处理器数量 */
	private final int availableProcessors = MBeanUtils.getOsInfo().getAvailableProcessors();

	/** wsApi的定时任务 */
	private Runnable wsApiServerJob;

	public void setWsApiServerJob(Runnable wsApiServerJob) {
		this.wsApiServerJob = wsApiServerJob;
	}

	@PostConstruct
	protected void init() {
		long now = System.currentTimeMillis();

		long intervalMs = TimeUnit.SECONDS.toMillis(TIME_INTERVAL);
		long initialDelay = intervalMs - now % intervalMs; // 延时的时间（毫秒）

		// 启动时就增加一个点
		this.addCurTimeHistoryPoint();

		this.scheduledExecutorService.scheduleAtFixedRate(() -> {
			if (this.wsApiServerJob != null) {
				// 如果有ws api，这通知ws那边记录一下历史信息
				this.wsApiServerJob.run();
			}

			// 定时执行添加时间点的任务
			this.addCurTimeHistoryPoint();
		}, initialDelay, intervalMs, TimeUnit.MILLISECONDS);
	}

	public PayloadCounter getWsUpCounter() {
		return wsUpCounter;
	}

	public PayloadCounter getWsDownCounter() {
		return wsDownCounter;
	}

	public PayloadCounter getActionCounter() {
		return actionCounter;
	}

	/** 增加发包流量 */
	public void addSendPacket(long payload) {
		this.hasSendPacketData = true;
		this.sendPacketCounter.addPayload(payload, 0);
	}

	/** 是否有发包数据 */
	public boolean isHasSendPacketData() {
		return hasSendPacketData;
	}

	public void setHasSendPacketData(boolean hasSendPacketData) {
		this.hasSendPacketData = hasSendPacketData;
	}

	public void reset() {
		this.actionCounter.reset();
		this.wsDownCounter.reset();
		this.wsUpCounter.reset();
		this.sendPacketCounter.reset();

		// 要重置上一个时间点的数据
		this.lastSnapShot.reset();
	}

	/** 获取历史运行状态 */
	public LinkedList<RuntimeInfoBean> getRuntimeHistory() {
		synchronized (this.history) {
			LinkedList<RuntimeInfoBean> list = new LinkedList<>(this.history); // 先添加历史
			return list;
		}
	}

	/** 获取当前的累计值 */
	private SnapshotBean createSnapshot() {
		SnapshotBean status = new SnapshotBean();

		status.wsDownCount = this.wsDownCounter.getCount();
		status.wsDownPayload = this.wsDownCounter.getPayloadTotal();
		status.wsUpCount = this.wsUpCounter.getCount();
		status.wsUpPayload = this.wsUpCounter.getPayloadTotal();
		status.actionCount = this.actionCounter.getCount();
		status.sendPacketCount = this.sendPacketCounter.getCount();
		status.sendPacketPayload = this.sendPacketCounter.getPayloadTotal();

		return status;
	}

	/** 获得当前时间和最后状态的差异值 */
	private RuntimeInfoBean calcDiff(SnapshotBean curSnapshot) {
		RuntimeInfoBean point = new RuntimeInfoBean();

		// 设置固定值
		point.setThreadCount(ManagementFactory.getThreadMXBean().getThreadCount()); // 线程数
		point.setMemory(OsUtil.getMemoryInfo()); // 内存

		// 各项差值
		point.setWsDownCount(curSnapshot.wsDownCount - this.lastSnapShot.wsDownCount);
		point.setWsDownPayload(curSnapshot.wsDownPayload - this.lastSnapShot.wsDownPayload);
		point.setWsUpCount(curSnapshot.wsUpCount - this.lastSnapShot.wsUpCount);
		point.setWsUpPayload(curSnapshot.wsUpPayload - this.lastSnapShot.wsUpPayload);
		point.setActionCount(curSnapshot.actionCount - this.lastSnapShot.actionCount);
		point.setSendPacketCount(curSnapshot.sendPacketCount - this.lastSnapShot.sendPacketCount);
		point.setSendPacketPayload(curSnapshot.sendPacketPayload - this.lastSnapShot.sendPacketPayload);

		long elapsedCpu = curSnapshot.processCpuTime - this.lastSnapShot.processCpuTime; // 获取cpu消耗的差值
		long elaspedTime = curSnapshot.uptime - this.lastSnapShot.uptime; // 获取时间的差值
		if (elaspedTime > 0) {
			// 计算CPU负载
			double cpuUsage = Math.min(99d, elapsedCpu / (elaspedTime * 10000d * this.availableProcessors));
			point.setProcessCpuLoad(cpuUsage);
		}

		return point;

	}

	/** 计算当前时间点的运行状态 */
	protected void addCurTimeHistoryPoint() {

		long now = System.currentTimeMillis();
		long timeInterval = TIME_INTERVAL * 1000;
		long recordTime = now / timeInterval * timeInterval; // 时间取整

		// 先记录当前状态快照
		SnapshotBean curSnapshot = this.createSnapshot();

		// 计算当前数值和上一个时间点的差值
		RuntimeInfoBean point = this.calcDiff(curSnapshot);
		point.setRecordTime(recordTime); // 修改修改一下时间

		// 先更新最后状态
		this.lastSnapShot = curSnapshot;

		// 加入到历史
		synchronized (this.history) {
			this.history.add(point);
		}
	}

	@PreDestroy
	protected void shutdown() {
		BaseThreadPool.shutdownExecutorService("运行状态定时任务", this.scheduledExecutorService);
	}

}
