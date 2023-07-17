package com.liangwj.tools2k.apiServer.beans;

import java.util.Date;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.apiServer.websocket.beans.ResponseTypeEnum;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 网络包基类
 * </pre>
 * 
 * @author rock
 * 
 */
@Setter
@Getter
public abstract class BaseSocketPackage {

	/** 0: 调用方法的返回结果，-1: 调用方法时发生了错误, 1: 这个是个推送的事件, */
	@AComment("0: 调用方法的返回结果，-1: 调用方法时发生了错误, 1: 这个是个推送的事件,")
	private final ResponseTypeEnum msgType;

	@AComment("响应生成的时间")
	private final Date createTime = new Date();

	public BaseSocketPackage(ResponseTypeEnum msgType) {
		this.msgType = msgType;
	}

	public int getMsgType() {
		return msgType.getCode();
	}

}
