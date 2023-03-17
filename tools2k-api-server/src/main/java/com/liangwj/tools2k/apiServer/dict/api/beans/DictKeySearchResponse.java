package com.liangwj.tools2k.apiServer.dict.api.beans;

import java.util.List;

import com.liangwj.tools2k.annotation.api.AMock;
import com.liangwj.tools2k.apiServer.beans.BaseResponse;

/**
 * <pre>
 * 关键词搜索的结果
 * </pre>
 * 
 * @author rock 2016年11月16日
 */
public class DictKeySearchResponse extends BaseResponse {

	private int pageSize;
	private int pageNo = 1;
	private int pageTotal;// 总页数
	private int itemTotal;// 总记录数

	private List<DictVo> list;

	@AMock("11")
	public int getItemTotal() {
		return itemTotal;
	}

	@AMock(size = 2)
	public List<DictVo> getList() {
		return list;
	}

	@AMock("1")
	public int getPageNo() {
		return pageNo;
	}

	@AMock("5")
	public int getPageSize() {
		return pageSize;
	}

	@AMock("3")
	public int getPageTotal() {
		return pageTotal;
	}

	public void setItemTotal(int itemTotal) {
		this.itemTotal = itemTotal;
	}

	public void setList(List<DictVo> list) {
		this.list = list;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}

}
