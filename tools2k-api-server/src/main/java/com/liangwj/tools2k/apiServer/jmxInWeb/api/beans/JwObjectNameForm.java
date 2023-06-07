package com.liangwj.tools2k.apiServer.jmxInWeb.api.beans;

import com.liangwj.tools2k.annotation.form.AForm;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * <pre>
 * MBean操作的form的基类
 * </pre>
 * 
 * @author<a href="https://github.com/liangwj72">Alex (梁韦江)</a> 2015年10月15日
 */
@AForm
public class JwObjectNameForm {

	@NotNull(message = "objectName不能为空")
	@Size(min = 1, message = "objectName不能为空")
	private String objectName;

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
}
