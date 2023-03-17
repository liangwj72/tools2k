package com.liangwj.tools2k.apiServer.dict.api.beans;

import com.liangwj.tools2k.apiServer.beans.BaseResponse;

/**
 * <pre>
 * 保存字典的返回
 * </pre>
 * 
 * @author rock 2016年11月17日
 */
public class DictSaveResponse extends BaseResponse {

	private DictVo data;

	public DictVo getData() {
		return data;
	}

	public void setData(DictVo data) {
		this.data = data;
	}

}
