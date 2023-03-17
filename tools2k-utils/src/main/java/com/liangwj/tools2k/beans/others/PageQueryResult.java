package com.liangwj.tools2k.beans.others;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.springframework.util.Assert;

import com.liangwj.tools2k.beans.form.IPageForm;
import com.liangwj.tools2k.utils.other.ConverterUtil;

/**
 * 用于存储分页查询的结果
 * 
 * @author rock
 *
 * @param <T>
 */
public class PageQueryResult<T> implements Serializable {

	private static final long serialVersionUID = 2886257609486632909L;

	private final int pageSize;

	private int pageNo = 1;
	private int pageTotal;// 总页数
	private final int itemTotal;// 总记录数
	private final List<T> list; // 数据

	public PageQueryResult(List<T> list, IPageForm form) {

		form.verifyPageNo();
		int start = (form.getPageNo() - 1) * form.getPageSize();// 分页时的起点
		int end = start + form.getPageSize();
		end = end > list.size() ? list.size() : end;

		List<T> res = new LinkedList<>();
		for (int i = start; i < end; i++) {
			res.add(list.get(i));
		}

		this.list = res;
		this.itemTotal = list.size();
		this.pageNo = form.getPageNo();
		this.pageSize = form.getPageSize();
		calc();
	}

	public PageQueryResult(int itemTotal, List<T> list, IPageForm form) {
		this(itemTotal, list, form.getPageNo(), form.getPageSize());
	}

	public PageQueryResult(int itemTotal, List<T> list, int pageno, int pageSize) {
		Assert.notNull(list, "数据list不能为空");

		this.list = list;
		this.itemTotal = itemTotal;
		this.pageNo = pageno;
		this.pageSize = pageSize;

		calc();
	}

	/**
	 * 根据参数计算总页数，当前页数等
	 */
	private void calc() {
		if (itemTotal > 0) {
			pageTotal = itemTotal / pageSize;
			if (itemTotal % pageSize != 0)
				pageTotal++;
			if (pageNo < 1) {
				this.pageNo = 1;
			}
		} else {
			this.pageTotal = 0;
		}
		if (pageNo > pageTotal) {
			pageNo = pageTotal;
		}

	}

	public int getItemTotal() {
		return itemTotal;
	}

	public int getPageNo() {
		return pageNo;
	}

	public int getPageTotal() {
		return pageTotal;
	}

	public List<T> getList() {
		return list;
	}

	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 将分页的结果转换成为另外一个
	 * 
	 * @param converter
	 *            转换器
	 * @return
	 */
	public <TARGET> PageQueryResult<TARGET> convert(IConverter<T, TARGET> converter) {

		// 将list转化一下
		List<TARGET> targetList = ConverterUtil.convertList(this.list, converter);

		// 重新包装 PageQueryResult
		PageQueryResult<TARGET> res = new PageQueryResult<>(this.itemTotal, targetList, this.pageNo, this.pageSize);

		return res;

	}

}
