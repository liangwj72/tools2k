package com.liangwj.tools2k.apiServer.security;

import com.liangwj.tools2k.apiServer.loginCheck.IWebUser;

/**
 * <pre>
 * 通用的系统管理用户，目前用于字典项目和jmx项目
 * </pre>
 * 
 * @author rock 2016年11月16日
 */
public class CommonAdminWebUser extends MyWebUser {

	private static final long serialVersionUID = 1L;

	/** 权限名字，用于中心服务器自动登录 */
	public final static String RIGHTS_NAME = "CommonAdminWebUser";

	private final IWebUser webUser;

	private boolean superUser;

	public CommonAdminWebUser(IWebUser webUser) {
		this.webUser = webUser;
	}

	public boolean isSuperUser() {
		return superUser;
	}

	public void setSuperUser(boolean superUser) {
		this.superUser = superUser;
	}

	@Override
	public boolean checkRights(String optId) {
		return true;
	}

	@Override
	public String getAccount() {
		return this.webUser.getAccount();
	}

	@Override
	public String getName() {
		return this.webUser.getName();
	}

	@Override
	public String getEncryptedPassword() {
		return this.webUser.getEncryptedPassword();
	}

}
