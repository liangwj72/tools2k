package com.liangwj.tools2k.apiServer.ajax.api.beans;

import javax.servlet.http.HttpServletRequest;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.apiServer.security.CommonAdminWebUser;
import com.liangwj.tools2k.utils.spring.WebContextHolderHelper;

@AComment("通用管理用户信息")
public class CommonAdminUserInfoBean {

	private final CommonAdminWebUser user;

	@AComment("放在header中的 x-auth-token值")
	private String xAuthToken;

	public CommonAdminUserInfoBean(CommonAdminWebUser user) {
		super();
		this.user = user;

		HttpServletRequest req = WebContextHolderHelper.getRequest();
		if (req != null) {
			this.xAuthToken = req.getSession().getId();
		}
	}

	public String getxAuthToken() {
		return xAuthToken;
	}

	@AComment("账号")
	public String getAccount() {
		return this.user.getAccount();
	}

	@AComment("名字")
	public String getName() {
		return this.user.getName();
	}

	@AComment("是否超级管理员")
	public boolean isSuperUser() {
		return this.user.isSuperUser();
	}
}
