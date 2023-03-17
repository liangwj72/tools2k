package com.liangwj.tools2k.apiServer.utils;


import org.junit.jupiter.api.Test;

import com.alibaba.fastjson.JSON;
import com.liangwj.tools2k.apiServer.beans.CommonSuccessResponse;
import com.liangwj.tools2k.utils.classUtils.MockDataCreater;

/**
 * <pre>
 * MockDataCreater的测试，测试生成模拟数据
 * </pre>
 *
 * @author rock 2016年8月25日
 */
public class MockDataCreaterTest {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MockDataCreaterTest.class);

	@Test
	public void test() {

	}

	public void test2() {
		CommonSuccessResponse res = MockDataCreater.newInstance(CommonSuccessResponse.class);
		log.debug("JSON:\n{}", JSON.toJSONString(res, true));
	}

}
