package com.liangwj.tools2k.beans.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.annotation.form.AForm;

/**
 * <pre>
 * 用于生成加密后密码的表单
 * </pre>
 * 
 * @author rock 2016年12月1日
 */
@AForm
public class CreatePasswordForm {

	@NotNull(message = "密码不能为空")
	@Size(min = 6, message = "长度不能小于6位")
	private String password;

	public String getPassword() {
		return password;
	}

	@AComment(value = "生成这个密码的加密字符串")
	public void setPassword(String password) {
		this.password = password;
	}

}
