package com.liangwj.tools2k.beans.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.annotation.api.AMock;
import com.liangwj.tools2k.annotation.form.AForm;

/**
 * <pre>
 * 只有一个电话号码的表单，用户发送手机验证码
 * </pre>
 * 
 * @author rock
 */
@AForm
public class PhoneForm {
	/**
	 * 电话号码
	 */
	@NotNull(message = "电话号码不能为空")
	@Size(min = 11, max = 11, message = "手机号码为11位")
	private String phone;

	public String getPhone() {
		return phone;
	}

	@AComment(value = "手机号码")
	@AMock("18620441223")
	public void setPhone(String phone) {
		this.phone = phone;
	}
}
