package com.liangwj.tools2k.beans.exceptions;

/**
 * <pre>
 * 发送短信时，发生了故障，例如发送次数超过限制了
 * </pre>
 * 
 * @author rock
 *  2016年8月3日
 */
public class SystemErrorWhenSendSmsException extends BaseApiException {

	private static final long serialVersionUID = 1L;

	@Override
	public String getErrorMsg() {
		return "系统繁忙，请稍后再试";
	}

}
