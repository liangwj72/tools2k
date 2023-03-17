package com.liangwj.tools2k.apiServer.jmxInWeb.api.beans;

import java.util.LinkedList;
import java.util.List;

import com.liangwj.tools2k.apiServer.beans.BaseResponse;

/**
 * <pre>
 * 返回 所有的mbean
 * </pre>
 * 
 * @author rock 2017年4月25日
 */
public class JwMBeanListResponse extends BaseResponse {

	private List<DomainVo> list = new LinkedList<>();

	public List<DomainVo> getList() {
		return list;
	}

	public void setList(List<DomainVo> list) {
		this.list = list;
	}

}
