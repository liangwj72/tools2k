package com.liangwj.tools2k.beans.exceptions;

/**
 * <pre>
 * 验证码错误
 * </pre>
 * 
 * @author rock
 *  2016年8月3日
 */
public class InvalidVerifyCodeException extends BaseApiException {

	private static final long serialVersionUID = 1L;

	@Override
	public String getErrorMsg() {
		return "验证码错误";
	}

}
