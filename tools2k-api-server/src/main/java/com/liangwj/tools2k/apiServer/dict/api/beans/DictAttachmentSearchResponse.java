package com.liangwj.tools2k.apiServer.dict.api.beans;

import java.util.List;

import com.liangwj.tools2k.annotation.api.AMock;
import com.liangwj.tools2k.apiServer.beans.BaseResponse;

/**
 * <pre>
 * 根据关键词搜索附件的结果
 * </pre>
 * 
 * @author rock 2016年11月16日
 */
public class DictAttachmentSearchResponse extends BaseResponse {

	private List<DictAttachmentVo> list;

	@AMock(size = 2)
	public List<DictAttachmentVo> getList() {
		return list;
	}

	public void setList(List<DictAttachmentVo> list) {
		this.list = list;
	}

}
