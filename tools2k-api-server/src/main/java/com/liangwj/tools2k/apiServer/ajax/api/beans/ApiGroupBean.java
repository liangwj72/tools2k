package com.liangwj.tools2k.apiServer.ajax.api.beans;

import java.util.LinkedList;
import java.util.List;

import com.liangwj.tools2k.annotation.api.AComment;

@AComment("一个接口分组")
public class ApiGroupBean implements Comparable<ApiGroupBean> {
	@AComment("接口分组的key")
	private String infKey;
	@AComment("备注")
	private String memo;
	@AComment("这个分组下的所有api")
	private final List<ApiMethodBean> methods = new LinkedList<>();

	public String getInfKey() {
		return infKey;
	}

	public void setInfKey(String infKey) {
		this.infKey = infKey;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public List<ApiMethodBean> getMethods() {
		return methods;
	}

	public void addMethod(ApiMethodBean methodInfoBean) {
		this.methods.add(methodInfoBean);
	}

	@Override
	public int compareTo(ApiGroupBean o) {
		// 分组按key字母顺序排序
		return this.infKey.compareTo(o.infKey);
	}

}
