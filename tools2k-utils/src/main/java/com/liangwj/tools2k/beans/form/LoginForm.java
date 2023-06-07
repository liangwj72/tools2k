package com.liangwj.tools2k.beans.form;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.annotation.form.AForm;

import jakarta.validation.constraints.NotNull;

/**
 * <pre>
 * 登录用得表单
 * </pre>
 * 
 * @author rock 2016年9月8日
 */
@AForm
public class LoginForm {

	@AComment(value = "账号")
	@NotNull(message = "请输入账号")
	private String account;

	@AComment(value = "密码")
	@NotNull(message = "请输入密码")
	private String password;

	@AComment(value = "是否记住密码")
	private boolean rememberMe;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public boolean isRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
