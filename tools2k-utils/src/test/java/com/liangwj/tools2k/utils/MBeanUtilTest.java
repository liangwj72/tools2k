package com.liangwj.tools2k.utils;

import javax.management.JMException;

import org.junit.jupiter.api.Test;

import com.alibaba.fastjson.JSON;
import com.liangwj.tools2k.utils.other.MBeanUtils;

public class MBeanUtilTest {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MBeanUtilTest.class);

	// @Test
	public void test_getClassLoadingInfo() {
		Object obj = MBeanUtils.getClassLoadingInfo();
		this.printObj("类加载信息", obj);
	}

	@Test
	public void test_getThreadingInfo() throws JMException {
		// MBeanUtils.printMBeanAttr("java.lang:type=Threading");
		Object obj = MBeanUtils.getThreadingInfo();
		this.printObj("线程信息", obj);
	}

	// @Test
	public void test_getVmInfo() {
		Object obj = MBeanUtils.getVmInfo();
		this.printObj("虚拟机信息", obj);
	}

	// @Test
	public void test_getOsInfo() throws JMException {
		// MBeanUtils.printMBeanAttr("java.lang:type=OperatingSystem");
		this.printObj("操作系统信息", MBeanUtils.getOsInfo());
	}

	private void printObj(String msg, Object obj) {
		log.debug("{}={}", msg, JSON.toJSONString(obj, true));
	}
}
