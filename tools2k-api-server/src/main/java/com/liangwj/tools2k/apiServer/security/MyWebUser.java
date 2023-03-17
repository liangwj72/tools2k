package com.liangwj.tools2k.apiServer.security;

import com.liangwj.tools2k.apiServer.loginCheck.IWebUser;

public abstract class MyWebUser implements IWebUser {
	private static final long serialVersionUID = 1L;

	protected long optTime = System.currentTimeMillis();

	public Long getOptTime() {
		return optTime;
	}

	public void updOptTime() {
		this.optTime = System.currentTimeMillis();
	}

	public boolean overtime() {
		return System.currentTimeMillis() - optTime >= 60 * 1000 * 30;
	}

}
