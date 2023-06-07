package com.liangwj.tools2k.beans.form;

import com.liangwj.tools2k.annotation.api.AMock;
import com.liangwj.tools2k.annotation.form.AForm;

import jakarta.validation.constraints.NotNull;

/**
 * <pre>
 * 通用的IdForm
 * </pre>
 * 
 * @author rock
 *  2016年8月25日
 */
@AForm
public class IdForm {

	@NotNull(message = "id不能为空")
	private Integer id;

	public Integer getId() {
		return id;
	}

	@AMock(value = "1")
	public void setId(Integer id) {
		this.id = id;
	}

}
