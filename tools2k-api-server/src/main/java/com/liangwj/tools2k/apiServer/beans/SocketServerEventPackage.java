package com.liangwj.tools2k.apiServer.beans;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.apiServer.websocket.beans.ResponseTypeEnum;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 服务器事件包
 * </pre>
 * 
 * @author rock
 * 
 */
@Setter
@Getter
public class SocketServerEventPackage extends BaseSocketPackage {

	@AComment("事件的数据")
	private final Object data;

	@AComment("事件的名字")
	private final String eventName;

	public SocketServerEventPackage(Object event) {
		super(ResponseTypeEnum.Event);

		this.eventName = event.getClass().getSimpleName();
		this.data = event;
	}

}
