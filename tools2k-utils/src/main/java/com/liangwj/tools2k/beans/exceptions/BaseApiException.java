package com.liangwj.tools2k.beans.exceptions;

import com.liangwj.tools2k.annotation.api.ADataInApiException;

/**
 * api抛错的基类，必须有错误代码和错误信息
 * 
 * @author rock
 * 
 */
public abstract class BaseApiException extends Exception {

	private static final long serialVersionUID = 1L;

	public BaseApiException() {
		super();
	}

	public BaseApiException(String paramString, Throwable paramThrowable, boolean paramBoolean1, boolean paramBoolean2) {
		super(paramString, paramThrowable, paramBoolean1, paramBoolean2);
	}

	public BaseApiException(String paramString, Throwable paramThrowable) {
		super(paramString, paramThrowable);
	}

	public BaseApiException(String paramString) {
		super(paramString);
		this.errorMsg = paramString;
	}

	public BaseApiException(String paramString, Integer messageCode) {
		this(paramString);
		this.messageCode = messageCode;
	}

	public BaseApiException(Throwable paramThrowable) {
		super(paramThrowable);
		this.errorMsg = "访问微信接口时，出现了错误";
	}

	private String errorMsg;

	/**
	 * 错误的提示消息
	 * 
	 * @return 错误提示
	 */
	public String getErrorMsg() {
		return this.errorMsg;
	}

	@Override
	public String getMessage() {
		return getErrorMsg();
	}

	/**
	 * 获得返回码， 逻辑错误还是正常的返回200代码
	 * 
	 * @return
	 */
	public int getHttpStatusCode() {
		return 400;
	}

	@ADataInApiException
	protected Integer messageCode;

	public Integer getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(Integer messageCode) {
		this.messageCode = messageCode;
	}

}
