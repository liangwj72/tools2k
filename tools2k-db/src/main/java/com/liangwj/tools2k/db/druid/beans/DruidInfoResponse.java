package com.liangwj.tools2k.db.druid.beans;

import java.util.Map;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.apiServer.beans.BaseResponse;

/**
 * Druid 监控的详情
 *
 * @author rock
 *
 */
public class DruidInfoResponse extends BaseResponse {

	@AComment("详情")
	private Map<String, Object> info;

	public Map<String, Object> getInfo() {
		return info;
	}

	public void setInfo(Map<String, Object> info) {
		this.info = info;
	}

}
