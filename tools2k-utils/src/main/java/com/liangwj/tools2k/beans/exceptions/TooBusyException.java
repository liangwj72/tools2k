package com.liangwj.tools2k.beans.exceptions;

import com.liangwj.tools2k.annotation.api.ADataInApiException;

/**
 * <pre>
 * 需要间隔一段时间才能再次操作
 * </pre>
 * 
 * @author rock
 */
public class TooBusyException extends BaseApiException {

	private static final long serialVersionUID = 1L;

	/** 剩余的秒数, 需要返回给客户端 */
	@ADataInApiException
	private final long remainInSec;

	public TooBusyException(long remainInSec) {
		super();
		this.remainInSec = remainInSec;
	}

	public long getRemainInSec() {
		return remainInSec;
	}

	@Override
	public String getErrorMsg() {
		return "需要间隔" + this.remainInSec + "秒才能再次操作";
	}

}
