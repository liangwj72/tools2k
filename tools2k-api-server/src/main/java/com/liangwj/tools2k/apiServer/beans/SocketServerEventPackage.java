package com.liangwj.tools2k.apiServer.beans;

import com.liangwj.tools2k.annotation.api.AComment;

/**
 * <pre>
 * 服务器事件包
 * </pre>
 * 
 * @author rock
 * 
 */
public class SocketServerEventPackage extends BaseSocketPackage {

	@AComment("事件的数据")
	private final Object data;

	@AComment("事件的名字")
	private final String eventName;

	public SocketServerEventPackage(Object event) {
		super(1);

		this.eventName = event.getClass().getSimpleName();
		this.data = event;
	}

	public Object getData() {
		return data;
	}

	public String getEventName() {
		return eventName;
	}

}
