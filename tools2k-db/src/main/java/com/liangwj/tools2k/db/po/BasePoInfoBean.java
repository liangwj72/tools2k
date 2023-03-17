package com.liangwj.tools2k.db.po;

import java.util.Date;

import org.springframework.util.Assert;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.beans.others.DateBean;

/**
 * <pre>
 * 基于 BasePo的InfoBean
 * </pre>
 * 
 * @author rock
 * 
 */
public class BasePoInfoBean<T extends BasePo> {
	protected final T po;

	public BasePoInfoBean(T po) {

		Assert.notNull(po, "po is null");

		this.po = po;
	}

	protected DateBean createDateBean(Date date) {
		return new DateBean(date);
	}

	@AComment("id")
	public int getId() {
		return this.po.getId();
	}

	@AComment("创建时间")
	public DateBean getCreateTime() {
		return this.createDateBean(this.po.getCreateTime());
	}

}
