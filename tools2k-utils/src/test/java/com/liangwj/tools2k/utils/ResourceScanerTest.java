package com.liangwj.tools2k.utils;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.liangwj.tools2k.utils.classUtils.ResourceScaner;

/**
 * <pre>
 * 资源扫描器的测试用例
 * </pre>
 *
 * @author rock 2016年9月28日
 */
public class ResourceScanerTest {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ResourceScanerTest.class);


	@Test
	public void testJar() throws IOException {
		String prefix = "javassist/expr";
		List<String> scanResult = ResourceScaner.scan(prefix);

		log.debug("测试扫描 jar 文件中的内容：扫描结果:{}", this.printScanResultToStr(scanResult));
	}

	@Test
	public void testFiles() throws IOException {
		String prefix = "com.linzi.framework.ipSeeker";
		List<String> scanResult = ResourceScaner.scan(prefix);

		log.debug("测试扫描 classpath中的文件：扫描结果:{}", this.printScanResultToStr(scanResult));
	}


	private String printScanResultToStr(List<String> scanResult) {
		StringBuffer sb = new StringBuffer();
		for (String str : scanResult) {
			sb.append('\n');
			sb.append(str);
		}
		return sb.toString();
	}

}
