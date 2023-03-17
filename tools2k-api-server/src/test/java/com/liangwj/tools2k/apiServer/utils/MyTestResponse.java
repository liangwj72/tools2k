package com.liangwj.tools2k.apiServer.utils;

import java.util.List;

import com.liangwj.tools2k.annotation.api.AMock;

/**
 * <pre>
 * 用于测试的bean，只有getter
 * </pre>
 * 
 * @author rock
 *  2016年8月25日
 */
public class MyTestResponse {

	public int getId() {
		return 0;
	}

	@AMock(value = "来自于mock的数据")
	public String getCode() {
		return "这个是真实的";
	}

	public MyTestResponse getSameObj() {
		return null;
	}

	@AMock(size = 2)
	public List<MyTestBean> getBeanList() {
		return null;
	}

	@AMock(size = 3)
	public List<String> getMyStringList() {
		return null;
	}

	@AMock(size = 3)
	public List<Integer> getIntegerList() {
		return null;
	}

}
