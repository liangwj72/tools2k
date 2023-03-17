package com.liangwj.tools2k.utils;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import com.liangwj.tools2k.utils.other.EncryptException;
import com.liangwj.tools2k.utils.other.EncryptUtil;

/**
 * <pre>
 *
 * </pre>
 *
 * @author rock
 *  2016年7月6日
 */
public class EncryptUtilTest {

	private final static String DES_KEY = "我们都是好人";

	@Test
	public void test() throws EncryptException {

		String src = "{xxx:xxx}";

		String ascll = EncryptUtil.desEncryptAscllString(DES_KEY, src);
		Assert.notNull(ascll, "加密的结果不应该为null");

		String str = EncryptUtil.desDecryptAscll(DES_KEY, ascll);
		Assert.notNull(ascll, "解密的结果不应该为null");

		Assert.isTrue(src.equals(str), "解密后的字符串该和原来的一样");

	}

}
