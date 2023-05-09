package com.liangwj.tools2k.apiServer.websocket.beans;

import com.liangwj.tools2k.apiServer.loginCheck.IWebUser;

/**
 * <pre>
 * 用户登录的事件
 * </pre>
 * 
 * @author 梁韦江
 * 
 */
public class SimpleUserLoginEvent {

	private final String account;
	private final String name;

	public SimpleUserLoginEvent(IWebUser user) {
		super();
		this.account = user.getAccount();
		this.name = user.getName();
	}

	public String getAccount() {
		return account;
	}

	public String getName() {
		return name;
	}

}
