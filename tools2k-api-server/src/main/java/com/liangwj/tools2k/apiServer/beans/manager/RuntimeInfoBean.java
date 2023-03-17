package com.liangwj.tools2k.apiServer.beans.manager;

import java.io.Serializable;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.beans.system.OsMemoryInfoBean;

/**
 * <pre>
 * ws 运行状态一个时间点的值
 * </pre>
 * 
 * @author rock
 * 
 */
@AComment("一个时间点的运行状态")
public class RuntimeInfoBean implements Serializable {

	private static final long serialVersionUID = -7509975362349926582L;

	@AComment("记录时间点")
	private long recordTime;

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

	@AComment("jvm的负载")
	private double processCpuLoad;

	@AComment("发包次数")
	private long sendPacketCount;

	@AComment("发包带宽")
	private long sendPacketPayload;

	@AComment("内存使用信息")
	private OsMemoryInfoBean memory;

	@AComment("线程数量")
	private int threadCount;

	public int getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	public OsMemoryInfoBean getMemory() {
		return memory;
	}

	public void setMemory(OsMemoryInfoBean memory) {
		this.memory = memory;
	}

	public double getProcessCpuLoad() {
		return processCpuLoad;
	}

	public void setProcessCpuLoad(double processCpuLoad) {
		this.processCpuLoad = processCpuLoad;
	}

	public long getSendPacketCount() {
		return sendPacketCount;
	}

	public void setSendPacketCount(long sendPacketCount) {
		this.sendPacketCount = sendPacketCount;
	}

	public long getSendPacketPayload() {
		return sendPacketPayload;
	}

	public void setSendPacketPayload(long sendPacketPayload) {
		this.sendPacketPayload = sendPacketPayload;
	}

	public long getRecordTime() {
		return this.recordTime;
	}

	public void reset() {
		this.wsUpCount = 0;
		this.wsUpPayload = 0;
		this.wsDownCount = 0;
		this.wsDownPayload = 0;
	}

	public void setRecordTime(long recordTime) {
		this.recordTime = recordTime;
	}

	public long getWsUpCount() {
		return wsUpCount;
	}

	public void setWsUpCount(long upCount) {
		this.wsUpCount = upCount;
	}

	public long getWsUpPayload() {
		return wsUpPayload;
	}

	public long getActionCount() {
		return actionCount;
	}

	public void setActionCount(long actionCount) {
		this.actionCount = actionCount;
	}

	public void setWsUpPayload(long upPayload) {
		this.wsUpPayload = upPayload;
	}

	public long getWsDownCount() {
		return wsDownCount;
	}

	public void setWsDownCount(long downCount) {
		this.wsDownCount = downCount;
	}

	public long getWsDownPayload() {
		return wsDownPayload;
	}

	public void setWsDownPayload(long downPayload) {
		this.wsDownPayload = downPayload;
	}

	/** 是否当前秒 */
	public boolean curTimeSec() {
		long now = System.currentTimeMillis() / 1000 * 1000;
		return now == this.recordTime;
	}
}
