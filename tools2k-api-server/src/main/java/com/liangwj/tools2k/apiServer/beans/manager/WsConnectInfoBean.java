package com.liangwj.tools2k.apiServer.beans.manager;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.beans.others.DateBean;

/**
 * <pre>
 * web socket 连接信息
 * </pre>
 * 
 * @author rock
 * 
 */
public class WsConnectInfoBean {

	private final IConnHandlerInfo handler;

	public WsConnectInfoBean(IConnHandlerInfo handler) {
		super();
		this.handler = handler;
	}

	@AComment("连接时间")
	public DateBean getConnectTime() {
		return new DateBean(this.handler.getConnectTime());
	}

	@AComment("下行流量统计")
	public PayloadCounter getDownCounter() {
		return this.handler.getDownCounter();
	}

	@AComment("ip地址")
	public String getIpAddress() {
		return this.handler.getIpAddress();
	}

	@AComment("获取最后一次请求的时间")
	public DateBean getLastRequestTime() {
		return new DateBean(this.handler.getLastRequestTime());
	}

	@AComment("连接的sessionId")
	public String getSessionId() {
		return this.handler.getSessionId();
	}

	@AComment("上行流量统计")
	public PayloadCounter getUpCounter() {
		return this.handler.getUpCounter();
	}

	@AComment("登陆的用户账号")
	public String getUserAccount() {
		return this.handler.getUserAccount();
	}

	@AComment("登录的用户类型")
	public String getUserClassName() {
		Class<?> clazz = this.handler.getUserClass();
		if (clazz != null) {
			return clazz.getSimpleName();
		}
		return null;
	}

	@AComment("最近10秒接受的请求")
	public long getLastUpCountDiff() {
		return handler.getLastUpCountDiff();
	}

	@AComment("是否有用户登录")
	public boolean isUserLogined() {
		return this.getUserAccount() != null;
	}
}
