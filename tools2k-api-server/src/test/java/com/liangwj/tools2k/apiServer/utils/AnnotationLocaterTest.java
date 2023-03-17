package com.liangwj.tools2k.apiServer.utils;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import com.liangwj.tools2k.annotation.api.AClass;
import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.utils.classUtils.AnnotationLocater;

/**
 * <pre>
 * AnnotationLocater 测试用例
 * </pre>
 *
 * @author rock 2016年9月28日
 */
public class AnnotationLocaterTest {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AnnotationLocaterTest.class);

	@Test
	public void test() throws IOException {
		String prefix = BeanWithACommentAnno.class.getPackage().getName();

		List<Class<?>> scanResult = AnnotationLocater.getClassList(prefix, AComment.class, AClass.class);

		log.debug("测试扫描 带指定注解 的类：扫描结果:{}", this.printScanResultToStr(scanResult));

		Assert.isTrue(scanResult.size() == 2, "应该能扫描到两个");
	}

	private String printScanResultToStr(List<Class<?>> scanResult) {
		StringBuffer sb = new StringBuffer();
		for (Class<?> clazz : scanResult) {
			sb.append('\n');
			sb.append(clazz.getName());
		}
		return sb.toString();
	}

}
