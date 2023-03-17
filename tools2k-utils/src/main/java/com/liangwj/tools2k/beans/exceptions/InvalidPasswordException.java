package com.liangwj.tools2k.beans.exceptions;

/**
 * <pre>
 * 登陆时，密码错误
 * </pre>
 * 
 * @author rock
 *  2016年8月3日
 */
public class InvalidPasswordException extends BaseApiException {

	private static final long serialVersionUID = 1L;

	@Override
	public String getErrorMsg() {
		return "密码错误";
	}

}
