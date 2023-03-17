package com.liangwj.tools2k.apiServer.ajax.api;

import com.liangwj.tools2k.annotation.api.AClass;
import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.annotation.api.AMethod;
import com.liangwj.tools2k.apiServer.ajax.api.beans.CommonAdminUserLoginResponse;
import com.liangwj.tools2k.apiServer.beans.BoolResponse;
import com.liangwj.tools2k.apiServer.beans.CommonSuccessResponse;
import com.liangwj.tools2k.apiServer.beans.manager.ServerInfoResponse;
import com.liangwj.tools2k.beans.exceptions.BaseApiException;
import com.liangwj.tools2k.beans.exceptions.InvalidPasswordException;
import com.liangwj.tools2k.beans.form.CreatePasswordForm;
import com.liangwj.tools2k.beans.form.LoginForm;

/**
 * <pre>
 * 为后台的系统服务提供统一的用户登录服务
 * </pre>
 * 
 * @author rock
 * 
 */
@AClass
@AComment("框架-公开")
public interface ICommonPublic {
	@AMethod(comment = "登录")
	CommonAdminUserLoginResponse login(LoginForm form) throws InvalidPasswordException;

	@AMethod(comment = "获取当前用户信息")
	CommonAdminUserLoginResponse getCurUser() throws InvalidPasswordException;

	@AMethod(comment = "登出")
	CommonSuccessResponse logout();

	@AMethod(comment = "获取服务器信息")
	ServerInfoResponse getServerStatus() throws InvalidPasswordException;

	@AMethod(comment = "创建密码例子")
	CommonSuccessResponse passwordDemo(CreatePasswordForm form);

	@AMethod(comment = "判断操作时间")
	BoolResponse updOptTimeOut() throws BaseApiException;

}
