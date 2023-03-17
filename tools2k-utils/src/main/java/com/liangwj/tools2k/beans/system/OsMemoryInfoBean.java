package com.liangwj.tools2k.beans.system;

import java.io.Serializable;

import com.liangwj.tools2k.annotation.api.AComment;

/**
 * <pre>
 * 操作系统信息
 * </pre>
 * 
 * @author rock 2016年12月17日
 */
@AComment("内存信息")
public class OsMemoryInfoBean implements Serializable {

	private static final long serialVersionUID = 6178360881892025876L;

	/** 已经分配不代表已经使用 */
	@AComment("已分配内存")
	private long totalMemory;

	/** 还可分配的内存 */
	@AComment("还可分配的内存")
	private long freeMemory;

	/** 已经使用的内存 */
	@AComment("已经使用的内存")
	private long usedMemory;

	/** 最大可分配内存 */
	@AComment("最大可分配内存")
	private long maxMemory;

	public long getTotalMemory() {
		return totalMemory;
	}

	public void setTotalMemory(long totalMemory) {
		this.totalMemory = totalMemory;
	}

	public long getFreeMemory() {
		return freeMemory;
	}

	public void setFreeMemory(long freeMemory) {
		this.freeMemory = freeMemory;
	}

	public long getUsedMemory() {
		return usedMemory;
	}

	public void setUsedMemory(long usedMemory) {
		this.usedMemory = usedMemory;
	}

	public long getMaxMemory() {
		return maxMemory;
	}

	public void setMaxMemory(long maxMemory) {
		this.maxMemory = maxMemory;
	}

}
