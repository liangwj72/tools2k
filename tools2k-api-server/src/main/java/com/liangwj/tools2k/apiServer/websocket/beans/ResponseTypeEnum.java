package com.liangwj.tools2k.apiServer.websocket.beans;

public enum ResponseTypeEnum {
	Sccess(0), // 成功调用方法的返回结果
	Error(-1), // 调用方法时发生了错误
	Event(1); // 这个是个推送的事件

	private final int code;

	private ResponseTypeEnum(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

}
