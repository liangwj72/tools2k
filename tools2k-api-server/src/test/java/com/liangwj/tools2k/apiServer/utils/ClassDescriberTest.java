package com.liangwj.tools2k.apiServer.utils;


import org.junit.jupiter.api.Test;

import com.liangwj.tools2k.apiServer.beans.CommonSuccessResponse;
import com.liangwj.tools2k.utils.classUtils.ClassDescriber;

/**
 * <pre>
 * ClassDescriber的测试用例
 * </pre>
 *
 * @author rock 2017年5月9日
 */
public class ClassDescriberTest {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ClassDescriberTest.class);

	@Test
	public void testCreate() {
		log.debug("测试 ClassDescriber.create()");

		Class<?> clazz = CommonSuccessResponse.class;

		String str = ClassDescriber.create(clazz);

		log.debug("分析 {} 的结果是：\n {}", clazz.getName(), str);
	}

}
