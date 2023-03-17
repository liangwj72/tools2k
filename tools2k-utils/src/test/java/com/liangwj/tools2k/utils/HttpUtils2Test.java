package com.liangwj.tools2k.utils;

import java.io.IOException;

import javax.net.ssl.SSLHandshakeException;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import com.liangwj.tools2k.beans.others.ProxyInfo;
import com.liangwj.tools2k.utils.net.HttpUtilException;
import com.liangwj.tools2k.utils.net.HttpUtils2;

/**
 * <pre>
 * http util 测试
 * </pre>
 *
 * @author rock 2016年7月21日
 */
public class HttpUtils2Test {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(HttpUtils2Test.class);

	@Test
	public void testHttp() throws HttpUtilException, IOException {
		HttpUtils2 client = HttpUtils2.createInstance(ProxyInfo.create("127.0.0.1", 11080));
		String res = client.get("https://www.baidu.com", null, null);
		System.out.println(res);
	}

	@Test
	public void testsHttp1() throws HttpUtilException, IOException {
		String res;
		String ip = "192.168.1.64";
		int port = 3721;
		ProxyInfo proxyInfo = ProxyInfo.create(ip, port).setUsername("dante-socks").setPassword("123456");
		res = HttpUtils2.createInstance(proxyInfo).get("http://192.168.1.248", null, null);

		Assert.isTrue("192.168.1.64".equals(res), "通过1.64的代理去获取来访者IP时，服务器获得的ip应该是代理服务器的ip");

		System.out.println(res);
		res = HttpUtils2.createInstance(proxyInfo).get("https://www.baidu.com", null, null);
		System.out.println(res);
	}

	/**
	 * 测试访问证书非法的网站- 用代理
	 * 
	 * @throws HttpUtilException
	 * @throws IOException
	 */
	@Test
	public void testInvalidHttpsWithProxy() throws HttpUtilException, IOException {
		try {
			HttpUtils2 client = HttpUtils2.createInstance(ProxyInfo.create("127.0.0.1", 11080));
			client.get("https://18.167.222.131", null, null);
			throw new IOException("应该出错才对");
		} catch (SSLHandshakeException e) {
			log.info("访问证书非法网站正常，抛出了正确的错误");
		}
	}

	/**
	 * 测试访问证书非法的网站- 不用代理
	 * 
	 * @throws HttpUtilException
	 * @throws IOException
	 */
	@Test
	public void testInvalidHttps() throws HttpUtilException, IOException {
		try {
			HttpUtils2 client = HttpUtils2.createInstance(4000);
			client.get("https://18.167.222.131", null, null);

			throw new IOException("应该出错才对");

		} catch (SSLHandshakeException e) {
			log.info("访问证书非法网站正常，抛出了正确的错误");
		}
	}

	/**
	 * 测试访问证书非法的网站- 不用代理
	 * 
	 * @throws HttpUtilException
	 * @throws IOException
	 */
	@Test
	public void testPassInvalidHttps() throws HttpUtilException, IOException {
		try {
			HttpUtils2 client = HttpUtils2.createInstance(4000, true);
			client.get("https://18.167.222.131", null, null);

			// HttpUtil.request("https://18.167.222.131", null, false, null);

		} catch (HttpUtilException e) {
			log.info("访问证书非法网站正常，我们信任非法证书");
			Assert.isTrue(e.getStatusCode() == 404, "测试网站应该是返回404错误");

		} catch (SSLHandshakeException e) {
			throw new IOException("应该出错才对");
		}
	}

}
