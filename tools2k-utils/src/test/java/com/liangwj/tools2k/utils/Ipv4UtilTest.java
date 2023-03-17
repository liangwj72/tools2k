package com.liangwj.tools2k.utils;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import com.liangwj.tools2k.utils.net.Ipv4Util;

public class Ipv4UtilTest {

	@Test
	public void getAllAddrByNameTest() throws UnknownHostException {
		List<Inet4Address> list = Ipv4Util.getAllAddrByName("www.baidu.com");
		Assert.isTrue(list.size() == 2, "百度有两个ipv4的地址");
	}

	@Test
	public void compareTest1() throws UnknownHostException {
		Inet4Address a1 = Ipv4Util.getAddrByName("1.1.0.0");
		Inet4Address a2 = Ipv4Util.getAddrByName("255.255.1.1");

		Assert.isTrue(Ipv4Util.compare(a1, a2) < 0, "小于");
	}

	@Test
	public void compareTest2() throws UnknownHostException {
		Inet4Address a1 = Ipv4Util.getAddrByName("0.0.0.0");
		Inet4Address a2 = Ipv4Util.getAddrByName("1.1.1.1");

		Assert.isTrue(Ipv4Util.compare(a1, a2) > 0, "大于");
	}

	@Test
	public void compareTest3() throws UnknownHostException {
		Inet4Address a1 = Ipv4Util.getAddrByName("1.1.1.1");
		Inet4Address a2 = Ipv4Util.getAddrByName("1.1.1.1");

		Assert.isTrue(Ipv4Util.compare(a2, a1) == 0, "相等");
	}

	@Test
	public void ipv4ToLongTest() throws UnknownHostException {
		Inet4Address a1 = Ipv4Util.getAddrByName("255.255.1.1");
		long res = Ipv4Util.ipv4ToLong(a1);
		System.out.printf("%x", res);
	}

	@Test
	public void findMyAddrTest() throws Exception {
		System.out.println("测试 findMyAddr " + Ipv4Util.findMyAddr());
	}
}
