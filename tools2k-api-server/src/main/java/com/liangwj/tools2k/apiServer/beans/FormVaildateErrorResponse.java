package com.liangwj.tools2k.apiServer.beans;

import java.util.List;

import com.liangwj.tools2k.beans.exceptions.FormValidateErrorInfoBean;
import com.liangwj.tools2k.beans.exceptions.ValidateFormException;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 表单校验不通过的返回对象。
 *
 * </pre>
 * 
 * @author rock
 *  2016年6月28日
 */
@Setter
@Getter
public class FormVaildateErrorResponse extends CommonErrorResponse {
	/**
	 * 表单校验不通过的内容
	 */
	private final List<FormValidateErrorInfoBean> errors;

	public FormVaildateErrorResponse(ValidateFormException e) {
		super(e);
		this.errors = e.getErrors();
	}

}
