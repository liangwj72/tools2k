package com.liangwj.tools2k.apiServer.dict.api.beans;

import com.liangwj.tools2k.apiServer.beans.BaseResponse;

/**
 * <pre>
 * 当前用户的基础信息
 * </pre>
 * 
 * @author rock 2016年11月14日
 */
public class UserInfoResponse extends BaseResponse {

	/** 当前登陆的账号名 */
	private String account;

	/** 是否已经登录 */
	private boolean logined;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public boolean isLogined() {
		return logined;
	}

	public void setLogined(boolean logined) {
		this.logined = logined;
	}

}
