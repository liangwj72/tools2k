package com.liangwj.tools2k.beans.exceptions;

import java.io.Serializable;

import com.liangwj.tools2k.annotation.api.ADataInApiException;

/**
 * <pre>
 * 找不到数据
 * </pre>
 * 
 * @author rock 2016年7月2日
 */
public class IdNotFoundException extends BaseApiException {

	private static final long serialVersionUID = 1L;

	private final String errorMsg;

	@ADataInApiException
	private final String id;

	public IdNotFoundException(String errorMsg, Serializable id) {
		super();
		this.errorMsg = errorMsg;
		this.id = String.valueOf(id);
	}

	@Override
	public String getErrorMsg() {
		return this.errorMsg;
	}

	public String getId() {
		return id;
	}

}
