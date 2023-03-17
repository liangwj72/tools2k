package com.liangwj.tools2k.beans.exceptions;

/**
 * <pre>
 * 简单类型的exception，仅仅就是一句话
 * </pre>
 * 
 * @author rock
 *  2016年7月2日
 */
public class SimpleApiException extends BaseApiException {

	private static final long serialVersionUID = 1L;

	private final String message;

	public SimpleApiException(String message) {
		super();
		this.message = message;
	}

	@Override
	public String getErrorMsg() {
		return this.message;
	}

}
