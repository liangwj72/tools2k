package com.liangwj.tools2k.apiServer.beans.manager;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.apiServer.beans.BaseResponse;

/**
 * <pre>
 * URL统计
 * </pre>
 *
 * @author rock
 *
 */
public class UrlStatResponse extends BaseResponse {
	@AComment("所有事件范围状态")
	private UrlStatInfoBean stat;

	public UrlStatInfoBean getStat() {
		return stat;
	}

	public void setStat(UrlStatInfoBean stat) {
		this.stat = stat;
	}

}
