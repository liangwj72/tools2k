package com.liangwj.tools2k.beans.system;

import com.liangwj.tools2k.annotation.api.AComment;

/**
 * <pre>
 * 磁盘信息，直接从当前目录中获取
 * </pre>
 * 
 * @author rock 2016年12月16日
 */
@AComment("")
public class DiskInfo {

	@AComment("空闲空间")
	private long freeSpace;
	@AComment("总空间")
	private long totalSpace;
	@AComment("可使用的空间")
	private long usableSpace;

	public long getFreeSpace() {
		return freeSpace;
	}

	public void setFreeSpace(long freeSpace) {
		this.freeSpace = freeSpace;
	}

	public long getTotalSpace() {
		return totalSpace;
	}

	public void setTotalSpace(long totalSpace) {
		this.totalSpace = totalSpace;
	}

	public long getUsableSpace() {
		return usableSpace;
	}

	public void setUsableSpace(long usableSpace) {
		this.usableSpace = usableSpace;
	}

}
