package com.liangwj.tools2k.apiServer.jmxInWeb.api.beans;

import javax.management.MBeanOperationInfo;

import com.liangwj.tools2k.annotation.api.AComment;

@AComment("描述一个mbean的opteration信息")
public class MBeanOperationInfoVo {
	private final MBeanOperationInfo info;

	public MBeanOperationInfoVo(MBeanOperationInfo info) {
		super();
		this.info = info;
	}

	@AComment("返回类型")
	public String getReturnType() {
		return this.info.getReturnType();
	}

	@AComment("说明")
	public String getDescription() {
		return this.info.getDescription();
	}

	@AComment("名字")
	public String getName() {
		return this.info.getName();
	}

}
