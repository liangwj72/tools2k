package com.liangwj.tools2k.apiServer.beans;

import java.util.Map;

import com.liangwj.tools2k.beans.exceptions.BaseApiException;

/**
 * <pre>
 * 调用发生异常时，通用的返回结果
 * </pre>
 *
 * @author rock 2016年6月28日
 */
public class CommonErrorResponse extends BaseResponse {

	/** BaseApiException 上额外的数据 */
	private Map<String, Object> exData;

	private final String errorCode;
	private final String errorMsg;

	public CommonErrorResponse(BaseApiException ex) {
		this.errorCode = ex.getClass().getSimpleName();
		this.errorMsg = ex.getErrorMsg();
	}

	public CommonErrorResponse(Throwable ex, String errorMessage) {
		this.errorCode = ex.getClass().getSimpleName();
		this.errorMsg = errorMessage;
	}

	public CommonErrorResponse(String errorMessage) {
		this.errorCode = "ERROR";
		this.errorMsg = errorMessage;
	}

	public String getErrorMsg() {
		return this.errorMsg;
	}

	public Map<String, Object> getExData() {
		return exData;
	}

	@Override
	public String getMessage() {
		return this.errorMsg;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setExData(Map<String, Object> apiData) {
		if (apiData == null || apiData.isEmpty()) {
			this.exData = null;
		} else {
			this.exData = apiData;
		}
	}
}
