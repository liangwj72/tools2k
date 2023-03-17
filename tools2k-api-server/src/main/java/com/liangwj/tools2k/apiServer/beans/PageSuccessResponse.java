package com.liangwj.tools2k.apiServer.beans;

/**
 * <pre>
 * 用于web页面的成功响应，主要是可以返回重定向的url
 * </pre>
 * 
 * @author rock
 *  2016年6月28日
 */
public class PageSuccessResponse extends BaseResponse {

	/** 成功后，重定向的url */
	private String redirectUrl;

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

}
