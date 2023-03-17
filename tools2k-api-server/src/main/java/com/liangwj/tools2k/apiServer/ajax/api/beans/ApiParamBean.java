package com.liangwj.tools2k.apiServer.ajax.api.beans;

import com.liangwj.tools2k.annotation.api.AComment;

/**
 * <pre>
 * 用于描述api form中每一个setter的信息
 * </pre>
 * 
 * @author rock 2016年7月4日
 */
public class ApiParamBean implements Comparable<ApiParamBean> {

	@AComment("参数名")
	private final String name;
	@AComment("默认值")
	private String value;
	@AComment("备注")
	private String memo;
	@AComment("是否文件上传字段")
	private boolean uploadFile;
	@AComment("参数的类型")
	private String className;
	@AComment(" 是否可空 ")
	private boolean notNull;
	@AComment(" 参数是否是数组 ")
	private boolean array;

	public ApiParamBean(String name) {
		this.name = name;
	}

	public boolean isArray() {
		return array;
	}

	public void setArray(boolean array) {
		this.array = array;
	}

	public boolean isNotNull() {
		return notNull;
	}

	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public boolean isUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(boolean uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@AComment("是否用checkbox表现")
	public boolean isCheckBox() {
		return "boolean".equalsIgnoreCase(this.className);
	}

	@Override
	public int compareTo(ApiParamBean o) {
		return name.compareTo(o.name);
	}

}
