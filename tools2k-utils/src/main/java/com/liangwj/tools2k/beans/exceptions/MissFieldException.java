package com.liangwj.tools2k.beans.exceptions;

/**
 * <pre>
 * 缺少某字段
 * </pre>
 * 
 * @author rock
 *  2016年9月2日
 */
public class MissFieldException extends BaseApiException {

	private static final long serialVersionUID = 1L;

	private final String message;

	public MissFieldException(String message) {
		super();
		this.message = message;
	}

	@Override
	public String getErrorMsg() {
		return this.message;
	}

}
