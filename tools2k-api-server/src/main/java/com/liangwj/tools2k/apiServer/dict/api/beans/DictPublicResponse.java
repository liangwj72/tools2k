package com.liangwj.tools2k.apiServer.dict.api.beans;

import java.util.Map;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.apiServer.beans.BaseResponse;

/**
 * <pre>
 * 对公开接口的内容
 * </pre>
 * 
 * @author rock 2016年11月14日
 */
@AComment("返回所有字典定义内容")
public class DictPublicResponse extends BaseResponse {

	@AComment("所有的数据")
	private Map<String, String> data;

	@AComment("服务器是否debug模式")
	private boolean debugMode;

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	public boolean isDebugMode() {
		return debugMode;
	}

	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}
}
