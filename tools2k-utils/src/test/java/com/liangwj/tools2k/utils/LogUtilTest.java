package com.liangwj.tools2k.utils;


import org.junit.jupiter.api.Test;

import com.liangwj.tools2k.beans.exceptions.SimpleApiException;
import com.liangwj.tools2k.utils.other.LogUtil;

/**
 * <pre>
 *
 * </pre>
 *
 * @author rock
 * 2016年9月8日
 */
public class LogUtilTest {

	// @Test
	public void getTraceString() {
		try {
			m1();
		} catch (Exception e) {
			System.out.println(LogUtil.getTraceString(null, e));
		}
	}

	@Test
	public void getStackTrace() {
		this._getStackTrace();
	}

	private void _getStackTrace() {
		System.out.println(LogUtil.getStackTrace("测试"));
	}

	private void m1() throws Exception {
		try {
			m2();
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	private void m2() throws Exception {
		throw new SimpleApiException("测试");
	}

}
