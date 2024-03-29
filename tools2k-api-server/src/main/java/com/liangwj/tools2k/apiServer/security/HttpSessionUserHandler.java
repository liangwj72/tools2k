package com.liangwj.tools2k.apiServer.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.liangwj.tools2k.apiServer.loginCheck.IWebUser;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <pre>
 * 将用户放到session中的处理器
 * </pre>
 * 
 * @author rock
 *  2016年8月23日
 */
@Component
public class HttpSessionUserHandler {

	@Autowired(required = false)
	private ILoginSuccessCallBack loginSuccessCallBack;

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(HttpSessionUserHandler.class);

	public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, IWebUser user) {
		log.debug("将用户 {} 的信息保存到Session中", user.getAccount());

		String sessionName = getSessionName(request, user.getClass());
		request.getSession().setAttribute(sessionName, user);

		// 如果外部有实现登录成功的回调，就调用回调
		if (this.loginSuccessCallBack != null) {
			this.loginSuccessCallBack.onLoginSuccess(user);
		}
	}

	public void onLogout(HttpServletRequest request, HttpServletResponse response, Class<? extends IWebUser> clazz) {

		log.debug("从 Session 中移除 {} 类型用户的值 ", clazz.getSimpleName());

		// logout 时，从session中删除用户信息
		String sessionName = getSessionName(request, clazz);
		request.getSession().removeAttribute(sessionName);
	}

	public <T extends IWebUser> T getUser(HttpServletRequest request, Class<? extends IWebUser> clazz) {
		String sessionName = getSessionName(request, clazz);

		@SuppressWarnings("unchecked")
		T user = (T) request.getSession().getAttribute(sessionName);
		return user;
	}

	/**
	 * 根据用户类获得Session中的名字
	 * 
	 * @param clazz
	 * @return
	 */
	private String getSessionName(HttpServletRequest request, Class<? extends IWebUser> clazz) {

		String sessionName = "user_" + clazz.getName();
		return sessionName;
	}

}
