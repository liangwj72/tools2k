package com.liangwj.tools2k.utils.net;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.util.StringUtils;

/**
 * 用于构造 HttpClient的参数列表
 */
public class HttpParamBuilder {
	public static HttpParamBuilder create() {
		return new HttpParamBuilder();
	}

	private final List<NameValuePair> list = new LinkedList<>();

	private HttpParamBuilder() {

	}

	public HttpParamBuilder addParam(String name, String value) {
		if (StringUtils.hasText(name)) {
			this.list.add(new BasicNameValuePair(name, value));
		}
		return this;
	}

	public List<NameValuePair> build() {
		return this.list;
	}
}
