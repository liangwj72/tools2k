package com.liangwj.tools2k.apiServer.ajax.api.beans;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.apiServer.beans.BaseResponse;

/**
 * <pre>
 * 通用管理账号登陆成功
 * </pre>
 * 
 * @author rock
 * 
 */
public class CommonAdminUserLoginResponse extends BaseResponse {

	@AComment("用户信息")
	private CommonAdminUserInfoBean curUser;

	public CommonAdminUserInfoBean getCurUser() {
		return curUser;
	}

	public void setCurUser(CommonAdminUserInfoBean curUser) {
		this.curUser = curUser;
	}

}
