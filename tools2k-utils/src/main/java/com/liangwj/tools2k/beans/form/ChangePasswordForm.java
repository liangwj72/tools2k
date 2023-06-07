package com.liangwj.tools2k.beans.form;


import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.annotation.form.AForm;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * <pre>
 * 修改密码的表单，修改密码的时候，必须将原密码传过来，做一次验证
 * </pre>
 * 
 * @author rock
 *  2016年8月3日
 */
@AForm
public class ChangePasswordForm {

	/**
	 * 旧的密码
	 */
	@NotNull(message = "旧密码不能为空")
	private String oldPassword;

	/**
	 * 新的密码
	 */
	@NotNull(message = "新密码不能为空")
	@Size(min = 5, message = "密码不能少于5位")
	private String newPassword;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	@AComment(value = "密码不能少于6位")
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
