package com.liangwj.tools2k.apiServer.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.liangwj.tools2k.apiServer.beans.CommonSuccessResponse;
import com.liangwj.tools2k.utils.net.HttpUtil;
import com.liangwj.tools2k.utils.net.HttpUtilException;

/**
 * <pre>
 * http util 测试
 * </pre>
 *
 * @author rock
 *  2016年7月21日
 */
public class HttpUtilTest {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(HttpUtilTest.class);

	@Test
	public void testHttps() throws HttpUtilException, IOException {
		HttpUtil.getInstance().doExecute("https://xxx.hk.us2k.net", null,false, null);
	}
	
	@Test
	public void testHttpsNo() throws HttpUtilException, IOException {
		HttpUtil.getInstance().doExecute("https://192.168.1.248", null,false, null);
	}

	@Test
	public void testJson() throws IOException, HttpUtilException {
		String url = "http://localhost:8080/json";

		CommonSuccessResponse res = HttpUtil.requestJson(CommonSuccessResponse.class, url, null, false, null);
		log.debug("post {} 的结果:\n{}", url, res.getMessage());

	}

	public void testPostFile() throws IOException, HttpUtilException {
		String url = "http://localhost:8080/guest/upload";

		File file = new File("d:/my.pac");
		org.springframework.util.Assert.isTrue(file.exists(),"要上传的测试文件不存在");

		String res = HttpUtil.postFileUseHttpClient(url, file, "file", null);
		log.debug("post {} 的结果:\n{}", url, res);

	}

	public void testHttpsGet() throws HttpUtilException, IOException {
		String url = "https://www.baidu.com/";
		String res = HttpUtil.getInstance().doExecute(url, null, false, null);

		log.debug("获取 {} 的结果:\n{}", url, res);
	}

	public void testPost() throws IOException, HttpUtilException {
		String url = "http://localhost:8080/p1";
		Map<String, Object> map = new HashMap<>();
		map.put("name1", "value1");
		map.put("name2", "value2");

		String res = HttpUtil.request(url, map, true, null);
		log.debug("post {} 的结果:\n{}", url, res);
	}

}
