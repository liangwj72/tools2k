package com.liangwj.tools2k.apiServer.dict.api.beans;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.annotation.api.AMock;
import com.liangwj.tools2k.annotation.form.AForm;

/**
 * <pre>
 * 字典编辑表单
 * </pre>
 * 
 * @author rock 2016年11月16日
 */
@AForm
public class DictRowEditForm {

	@NotEmpty(message = "Key不能为空")
	private String key;

	@NotNull(message = "值不能为空")
	private String value;
	
	@NotNull(message = "类型不能为空，请选择内容类型")
	private int type;

	/** 备注 */
	private String memo;

	private boolean html;

	public boolean isHtml() {
		return html;
	}

	public String getKey() {
		return key;
	}

	public String getMemo() {
		return memo;
	}

	public String getValue() {
		return value;
	}
	
	public int getType() {
		return type;
	}

	@AComment(value = "是否是直接输出为html")
	@AMock("false")
	public void setHtml(boolean html) {
		this.html = html;
	}

	@AComment(value = "键值")
	public void setKey(String key) {
		this.key = key;
	}

	@AComment(value = "类型")
	public void setType(int type) {
		this.type = type;
	}

	@AComment(value = "备注")
	public void setMemo(String memo) {
		this.memo = memo;
	}

	@AComment(value = "值")
	public void setValue(String value) {
		this.value = value;
	}

}
