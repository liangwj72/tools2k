package com.liangwj.tools2k.db.druid.beans;

import java.util.List;
import java.util.Map;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.apiServer.beans.BaseResponse;

/**
 * Druid 监控的数据列表，返回的是List<Map<String, Object>>
 *
 * @author rock
 *
 */
public class DruidListResponse extends BaseResponse {

	@AComment("返回的列表")
	private List<Map<String, Object>> list;

	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}

}
