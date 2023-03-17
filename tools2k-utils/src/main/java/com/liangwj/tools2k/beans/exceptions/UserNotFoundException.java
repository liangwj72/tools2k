package com.liangwj.tools2k.beans.exceptions;

/**
 * <pre>
 * 用户找不到
 * </pre>
 * 
 * @author rock
 *  2016年8月3日
 */
public class UserNotFoundException extends BaseApiException {

	private static final long serialVersionUID = 1L;

	@Override
	public String getErrorMsg() {
		return "无此用户";
	}

}
