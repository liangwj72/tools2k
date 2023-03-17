package com.liangwj.tools2k.beans.exceptions;

/**
 * <pre>
 * 登陆状态失效了，需要重新授权
 * </pre>
 * 
 * @author rock
 *  2016年7月2日
 */
public class InvalidLoginStatusException extends BaseApiException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getErrorMsg() {
		return "登陆状态失效了，需要重新登陆";
	}

	@Override
	public int getHttpStatusCode() {
		return 401;
	}

}
