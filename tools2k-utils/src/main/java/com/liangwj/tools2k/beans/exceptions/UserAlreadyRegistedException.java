package com.liangwj.tools2k.beans.exceptions;

/**
 * <pre>
 * 该账号已经注册了
 * </pre>
 * 
 * @author rock 2016年9月5日
 */
public class UserAlreadyRegistedException extends BaseApiException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getErrorMsg() {
		return "该账号已经注册了";
	}

}
