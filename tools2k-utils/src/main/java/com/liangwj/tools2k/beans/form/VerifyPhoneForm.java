package com.liangwj.tools2k.beans.form;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.annotation.api.AMock;
import com.liangwj.tools2k.annotation.form.AForm;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * <pre>
 * 用于验证电话号码的表单
 * </pre>
 * 
 * @author rock
 * 
 */
@AForm
public class VerifyPhoneForm extends PhoneForm {

	@AComment(value = "4位短信验证码")
	@NotNull(message = "验证码不能为空")
	@Size(min = 4, max = 4, message = "验证码为4位")
	private String code;

	public String getCode() {
		return code;
	}

	@AMock("1234")
	public void setCode(String code) {
		this.code = code;
	}
}
