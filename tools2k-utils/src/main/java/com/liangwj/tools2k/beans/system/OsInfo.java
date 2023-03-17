package com.liangwj.tools2k.beans.system;

import java.io.Serializable;

import com.liangwj.tools2k.annotation.api.AComment;

/**
 * <pre>
 * 对应MBean: java.lang:type=OperatingSystem
 * </pre>
 * 
 * @author rock
 * 
 */
@AComment("操作系统信息")
public class OsInfo implements Serializable {
	private static final long serialVersionUID = -5520028730175985122L;

	@AComment(" 提交的虚拟内存")
	private long committedVirtualMemorySize;

	@AComment("空闲的物理内存")
	private long freePhysicalMemorySize;

	@AComment("空闲的交换空间")
	private long freeSwapSpaceSize;

	@AComment("进程导致的CPU负载")
	private double processCpuLoad;

	@AComment("CPU时间")
	private long processCpuTime;

	@AComment("系统的CPU负载")
	private double systemCpuLoad;

	@AComment("总物理内存")
	private long totalPhysicalMemorySize;

	@AComment("总交换空间")
	private long totalSwapSpaceSize;

	@AComment("操作系统名称")
	private java.lang.String name;

	@AComment("处理器数量")
	private int availableProcessors;

	@AComment("操作系统版本")
	private java.lang.String version;

	@AComment("体系结构")
	private java.lang.String arch;

	public long getCommittedVirtualMemorySize() {
		return committedVirtualMemorySize;
	}

	public void setCommittedVirtualMemorySize(long committedVirtualMemorySize) {
		this.committedVirtualMemorySize = committedVirtualMemorySize;
	}

	public long getFreePhysicalMemorySize() {
		return freePhysicalMemorySize;
	}

	public void setFreePhysicalMemorySize(long freePhysicalMemorySize) {
		this.freePhysicalMemorySize = freePhysicalMemorySize;
	}

	public long getFreeSwapSpaceSize() {
		return freeSwapSpaceSize;
	}

	public void setFreeSwapSpaceSize(long freeSwapSpaceSize) {
		this.freeSwapSpaceSize = freeSwapSpaceSize;
	}

	public double getProcessCpuLoad() {
		return processCpuLoad;
	}

	public void setProcessCpuLoad(double processCpuLoad) {
		this.processCpuLoad = processCpuLoad;
	}

	public long getProcessCpuTime() {
		return processCpuTime;
	}

	public void setProcessCpuTime(long processCpuTime) {
		this.processCpuTime = processCpuTime;
	}

	public double getSystemCpuLoad() {
		return systemCpuLoad;
	}

	public void setSystemCpuLoad(double systemCpuLoad) {
		this.systemCpuLoad = systemCpuLoad;
	}

	public long getTotalPhysicalMemorySize() {
		return totalPhysicalMemorySize;
	}

	public void setTotalPhysicalMemorySize(long totalPhysicalMemorySize) {
		this.totalPhysicalMemorySize = totalPhysicalMemorySize;
	}

	public long getTotalSwapSpaceSize() {
		return totalSwapSpaceSize;
	}

	public void setTotalSwapSpaceSize(long totalSwapSpaceSize) {
		this.totalSwapSpaceSize = totalSwapSpaceSize;
	}

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public int getAvailableProcessors() {
		return availableProcessors;
	}

	public void setAvailableProcessors(int availableProcessors) {
		this.availableProcessors = availableProcessors;
	}

	public java.lang.String getVersion() {
		return version;
	}

	public void setVersion(java.lang.String version) {
		this.version = version;
	}

	public java.lang.String getArch() {
		return arch;
	}

	public void setArch(java.lang.String arch) {
		this.arch = arch;
	}

}
