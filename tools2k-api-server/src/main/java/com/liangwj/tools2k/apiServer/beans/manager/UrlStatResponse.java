package com.liangwj.tools2k.apiServer.beans.manager;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.apiServer.beans.BaseResponse;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * URL统计
 * </pre>
 *
 * @author rock
 *
 */
@Setter
@Getter
public class UrlStatResponse extends BaseResponse {
	@AComment("所有事件范围状态")
	private UrlStatInfoBean stat;
}
