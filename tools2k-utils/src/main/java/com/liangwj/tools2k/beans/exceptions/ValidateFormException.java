package com.liangwj.tools2k.beans.exceptions;

import java.util.List;

import com.liangwj.tools2k.annotation.api.ADataInApiException;

/**
 * <pre>
 * 表单验证错误。验证方法在 BinderUtil中
 * </pre>
 * 
 * @author rock
 *  2016年8月24日
 */
public class ValidateFormException extends BaseApiException {

	private static final long serialVersionUID = 1L;

	/** 被验证的表单 */
	@ADataInApiException
	private final Object form;

	/** 错误信息 */
	@ADataInApiException
	private final List<FormValidateErrorInfoBean> errors;

	public ValidateFormException(Object form, List<FormValidateErrorInfoBean> errors) {
		this.form = form;
		this.errors = errors;
	}

	public Object getForm() {
		return form;
	}

	public List<FormValidateErrorInfoBean> getErrors() {
		return errors;
	}

	@Override
	public String getErrorMsg() {
		StringBuffer sb = new StringBuffer();

		for (FormValidateErrorInfoBean info : errors) {
			if (sb.length() > 0) {
				sb.append("\n");
			}
			sb.append(info.getErrorMsg());
		}

		return sb.toString();
	}

}
