package com.liangwj.tools2k.apiServer.websocket.beans;

/**
 * <pre>
 * 用户被踢下线的事件
 * </pre>
 * 
 * @author 梁韦江
 * 
 */
public class KickoutEvent {

	/** 原因 */
	private String reason = "你已经在另外一个地方上线了";

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
