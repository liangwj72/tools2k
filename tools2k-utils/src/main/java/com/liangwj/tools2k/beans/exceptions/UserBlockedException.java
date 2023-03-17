package com.liangwj.tools2k.beans.exceptions;

/**
 * <pre>
 * 用户已经被禁用
 * </pre>
 * 
 * @author rock 2016年8月3日
 */
public class UserBlockedException extends BaseApiException {

	private static final long serialVersionUID = 1L;

	@Override
	public String getErrorMsg() {
		return "该用户已经被禁用!";
	}

}
