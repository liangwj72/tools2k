package com.liangwj.tools2k.apiServer.jmxInWeb.api.beans;

import com.liangwj.tools2k.apiServer.beans.BaseResponse;

/**
 * <pre>
 * 获取一个Mbean信息的响应
 * </pre>
 * 
 * @author rock 2017年4月25日
 */
public class JwMBeanInfoResponse extends BaseResponse {

	private MBeanVo info;

	public MBeanVo getInfo() {
		return info;
	}

	public void setInfo(MBeanVo info) {
		this.info = info;
	}

}
