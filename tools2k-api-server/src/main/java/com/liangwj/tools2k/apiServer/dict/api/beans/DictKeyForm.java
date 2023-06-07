package com.liangwj.tools2k.apiServer.dict.api.beans;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.annotation.form.AForm;

import jakarta.validation.constraints.NotNull;

/**
 * <pre>
 * 字典key表单
 * </pre>
 * 
 * @author rock 2015年8月4日
 */
@AForm
public class DictKeyForm {

	@NotNull
	private String key;

	public String getKey() {
		return key;
	}

	@AComment(value = "关键词")
	public void setKey(String key) {
		this.key = key;
	}

}
