package com.liangwj.tools2k.apiServer.beans.manager;

import java.io.Serializable;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.beans.system.OsMemoryInfoBean;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * ws 运行状态一个时间点的值
 * </pre>
 * 
 * @author rock
 * 
 */
@AComment("一个时间点的运行状态")
@Setter
@Getter
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

	public void reset() {
		this.wsUpCount = 0;
		this.wsUpPayload = 0;
		this.wsDownCount = 0;
		this.wsDownPayload = 0;
	}

	/** 是否当前秒 */
	public boolean curTimeSec() {
		long now = System.currentTimeMillis() / 1000 * 1000;
		return now == this.recordTime;
	}
}
