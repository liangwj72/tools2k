package com.liangwj.tools2k.beans.system;

import java.io.Serializable;

import com.liangwj.tools2k.annotation.api.AComment;

@AComment("线程的信息")
public class ThreadingInfo implements Serializable {
	private static final long serialVersionUID = 8636078834555611575L;

	@AComment("活动线程数")
	private int threadCount;

	@AComment("峰值线程数")
	private int peakThreadCount;

	@AComment("守护程序线程")
	private int daemonThreadCount;

	@AComment("启动的线程总数")
	private long totalStartedThreadCount;

	public int getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	public int getPeakThreadCount() {
		return peakThreadCount;
	}

	public void setPeakThreadCount(int peakThreadCount) {
		this.peakThreadCount = peakThreadCount;
	}

	public int getDaemonThreadCount() {
		return daemonThreadCount;
	}

	public void setDaemonThreadCount(int daemonThreadCount) {
		this.daemonThreadCount = daemonThreadCount;
	}

	public long getTotalStartedThreadCount() {
		return totalStartedThreadCount;
	}

	public void setTotalStartedThreadCount(long totalStartedThreadCount) {
		this.totalStartedThreadCount = totalStartedThreadCount;
	}

}
