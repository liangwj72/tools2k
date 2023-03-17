package com.liangwj.tools2k.beans.exceptions;

/**
 * <pre>
 * 其他错误
 * </pre>
 * 
 * @author rock
 *  2016年7月2日
 */
public class SystemErrorException extends BaseApiException {

	public SystemErrorException(Throwable paramThrowable) {
		super(paramThrowable);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getErrorMsg() {
		return "系统发生内部错误";
	}

}
