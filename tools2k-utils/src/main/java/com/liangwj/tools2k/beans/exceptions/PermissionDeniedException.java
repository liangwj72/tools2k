package com.liangwj.tools2k.beans.exceptions;

/**
 * <pre>
 * 无此权限，继承于 InvalidLoginStatusException
 * </pre>
 * 
 * @see InvalidLoginStatusException
 * 
 * @author rock
 *  2016年7月2日
 */
public class PermissionDeniedException extends InvalidLoginStatusException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getErrorMsg() {
		return "对不起，你没有权限进行该操作，请重新登陆";
	}

	@Override
	public int getHttpStatusCode() {
		return 403;
	}

}
