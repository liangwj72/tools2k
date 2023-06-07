package com.liangwj.tools2k.apiServer.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <pre>
 * LoginCheck扩展的检查器
 * </pre>
 * 
 * @author rock
 * 
 */
public interface ILoginCheckExtService {

	/**
	 * 在检查权限之前执行
	 */
	public void beforeCheckRight(ActionInfo info, HttpServletResponse response, HttpServletRequest request)
			throws LoginExtCheckException;

	/**
	 * 在检查权限之后执行
	 */
	public void afterCheckRight(ActionInfo info, HttpServletResponse response, HttpServletRequest request)
			throws LoginExtCheckException;

}
