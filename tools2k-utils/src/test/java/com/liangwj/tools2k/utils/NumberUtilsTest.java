package com.liangwj.tools2k.utils;

import org.junit.jupiter.api.Test;

import com.liangwj.tools2k.utils.other.NumberUtils;

public class NumberUtilsTest {

	@Test
	public void shortToIntTest() {
		short a = -1;
		System.out.println(NumberUtils.shortToInt(a));
	}
}
