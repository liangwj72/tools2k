package com.liangwj.tools2k.apiServer.beans;

import com.liangwj.tools2k.annotation.api.AComment;

import lombok.Getter;
import lombok.Setter;

/**
 * 响应返回结果基类
 * 
 * 2016年5月10日 下午2:01:17
 */
@Setter
@Getter
public abstract class BaseResponse {

	/**
	 * 必须有 message这个属性在基类，否则android 客户端没法用
	 */
	@AComment(value = "任何信息")
	private String message;

	@AComment(value = "发送时间")
	private final long time = System.currentTimeMillis();

	public boolean isSuccess() {
		return true;
	}
}
