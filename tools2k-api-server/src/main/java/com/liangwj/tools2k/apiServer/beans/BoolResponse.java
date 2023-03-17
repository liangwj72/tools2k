package com.liangwj.tools2k.apiServer.beans;

import com.liangwj.tools2k.annotation.api.AComment;

@AComment("布尔值信息")
public class BoolResponse extends BaseResponse {

	@AComment("布尔值属性")
	private Boolean ok;

	public BoolResponse(Boolean ok) {
		this.ok = ok;
	}

	public Boolean getOk() {
		return ok;
	}

	public void setOk(Boolean ok) {
		this.ok = ok;
	}

}
