package com.liangwj.tools2k.beans.form;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.annotation.api.AMock;
import com.liangwj.tools2k.annotation.form.ABuildWhereExclude;
import com.liangwj.tools2k.annotation.form.AForm;
import com.liangwj.tools2k.annotation.form.AFormValidateMethod;

/**
 * <pre>
 * 翻页查询的基础表单，所有方法在生成hsql时不作为sql的组成部分
 * </pre>
 * 
 * @author rock 2016年6月24日
 */
@AForm
public class PageForm implements IPageForm {

	/**
	 * 页码，默认是第一页
	 */
	private int pageNo = 1;

	/**
	 * 每页返回的记录数，默认是20
	 * 
	 * @see IPageForm#DEFAULT_PAGE_SIZE
	 */
	private int pageSize = IPageForm.DEFAULT_PAGE_SIZE;

	public PageForm() {
	}

	public PageForm(int pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	@ABuildWhereExclude
	public int getPageNo() {
		return pageNo;
	}

	@Override
	@ABuildWhereExclude
	public int getPageSize() {
		return pageSize;
	}

	@AComment(value = "页码")
	@AMock("1")
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	@AComment(value = "每页的大小")
	@AMock("5")
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	@AFormValidateMethod
	public void verifyPageNo() {
		if (this.pageNo < 1) {
			this.pageNo = 1;
		}
		if (this.pageSize < 1) {
			this.pageSize = 1;
		}
	}

}
