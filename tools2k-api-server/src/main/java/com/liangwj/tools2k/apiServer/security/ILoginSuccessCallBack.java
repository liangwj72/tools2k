package com.liangwj.tools2k.apiServer.security;

import com.liangwj.tools2k.apiServer.loginCheck.IWebUser;

/**
 * <pre>
 * 登录成功时的回调
 * </pre>
 * 
 * @author rock
 * 
 */
public interface ILoginSuccessCallBack {

	void onLoginSuccess(IWebUser user);

}
