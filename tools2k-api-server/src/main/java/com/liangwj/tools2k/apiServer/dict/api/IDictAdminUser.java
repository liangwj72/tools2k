package com.liangwj.tools2k.apiServer.dict.api;

import com.liangwj.tools2k.annotation.api.AClass;
import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.annotation.api.AMethod;
import com.liangwj.tools2k.apiServer.beans.CommonSuccessResponse;
import com.liangwj.tools2k.apiServer.dict.api.beans.UserInfoResponse;
import com.liangwj.tools2k.beans.exceptions.InvalidPasswordException;
import com.liangwj.tools2k.beans.form.CreatePasswordForm;
import com.liangwj.tools2k.beans.form.LoginForm;

/**
 * <pre>
 * 字典管理用户接口
 * </pre>
 * 
 * @author rock 2016年11月16日
 */
@AClass("dictAdmin")
@AComment(value = "字典-用户")
public interface IDictAdminUser {

	@AMethod(comment = "获得当前用户的信息，返回内容中包含是是否已登录的信息")
	UserInfoResponse getCurUser();

	@AMethod(comment = "登录")
	UserInfoResponse login(LoginForm form) throws InvalidPasswordException;

	@AMethod(comment = "登出")
	UserInfoResponse logout();

	@AMethod(comment = "创建密码例子")
	CommonSuccessResponse passwordDemo(CreatePasswordForm form);
}
