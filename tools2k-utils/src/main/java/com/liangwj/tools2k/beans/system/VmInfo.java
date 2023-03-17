package com.liangwj.tools2k.beans.system;

import java.io.Serializable;

import com.liangwj.tools2k.annotation.api.AComment;

@AComment("虚拟机的信息")
public class VmInfo implements Serializable {

	private static final long serialVersionUID = -3769126130276098254L;
	@AComment("虚拟机名")
	private String name;

	@AComment("vm参数")
	private String[] inputArguments;

	@AComment("虚拟机名字")
	private String vmName;

	@AComment("虚拟机供应商")
	private String vmVendor;

	@AComment("虚拟机版本")
	private String vmVersion;

	@AComment("java版本")
	private String specVersion;

	@AComment("启动时间")
	private long startTime;

	@AComment("运行时间")
	private long uptime;

	@AComment("这个bean生成的时间")
	private final long createTime = System.currentTimeMillis();

	public long getUptime() {
		return uptime;
	}

	public void setUptime(long uptime) {
		this.uptime = uptime;
	}

	public long getCreateTime() {
		return createTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getInputArguments() {
		return inputArguments;
	}

	public void setInputArguments(String[] inputArguments) {
		this.inputArguments = inputArguments;
	}

	public String getVmName() {
		return vmName;
	}

	public void setVmName(String vmName) {
		this.vmName = vmName;
	}

	public String getVmVendor() {
		return vmVendor;
	}

	public void setVmVendor(String vmVendor) {
		this.vmVendor = vmVendor;
	}

	public String getVmVersion() {
		return vmVersion;
	}

	public void setVmVersion(String vmVersion) {
		this.vmVersion = vmVersion;
	}

	public String getSpecVersion() {
		return specVersion;
	}

	public void setSpecVersion(String specVersion) {
		this.specVersion = specVersion;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

}
