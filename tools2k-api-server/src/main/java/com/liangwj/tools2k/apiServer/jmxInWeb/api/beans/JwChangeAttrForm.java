package com.liangwj.tools2k.apiServer.jmxInWeb.api.beans;

import com.liangwj.tools2k.annotation.form.AForm;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * <pre>
 * MBean操作的form
 * </pre>
 * 
 * @author<a href="https://github.com/liangwj72">Alex (梁韦江)</a> 2015年10月15日
 */
@AForm
public class JwChangeAttrForm extends JwObjectNameForm {

	@NotNull(message = "属性名不能为空")
	@Size(min = 1, message = "属性名不能为空")
	private String name;// attr name

	private String value;

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public void setName(String name) {
		this.name = name;
	}


	public void setValue(String value) {
		this.value = value;
	}

}
