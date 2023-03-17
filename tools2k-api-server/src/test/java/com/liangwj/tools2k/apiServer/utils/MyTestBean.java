package com.liangwj.tools2k.apiServer.utils;

import com.liangwj.tools2k.annotation.api.AMock;

/**
 * <pre>
 * 用于测试的bean，只有getter
 * </pre>
 * 
 * @author rock
 *  2016年8月25日
 */
public class MyTestBean {

	@AMock(id = true)
	public int getId() {
		return 0;
	}

	public String getMyString() {
		return null;
	}


}
