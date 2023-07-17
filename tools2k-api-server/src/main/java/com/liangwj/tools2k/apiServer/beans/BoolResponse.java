package com.liangwj.tools2k.apiServer.beans;

import com.liangwj.tools2k.annotation.api.AComment;

import lombok.Getter;
import lombok.Setter;

@AComment("布尔值信息")
@Setter
@Getter
public class BoolResponse extends BaseResponse {

	@AComment("布尔值属性")
	private boolean ok;

	public BoolResponse(boolean ok) {
		this.ok = ok;
	}

}
