package com.liangwj.tools2k.apiServer.beans;

import java.util.Date;

import com.liangwj.tools2k.annotation.api.AComment;

/**
 * <pre>
 * 网络包基类
 * </pre>
 * 
 * @author rock
 * 
 */
public abstract class BaseSocketPackage {

	/** 0: 调用方法的返回结果，-1: 调用方法时发生了错误, 1: 这个是个推送的事件, */
	@AComment("0: 调用方法的返回结果，-1: 调用方法时发生了错误, 1: 这个是个推送的事件,")
	private final int msgType;

	@AComment("响应生成的时间")
	private final Date createTime = new Date();

	public Date getCreateTime() {
		return createTime;
	}

	public BaseSocketPackage(int msgType) {
		this.msgType = msgType;
	}

	public int getMsgType() {
		return msgType;
	}

}
