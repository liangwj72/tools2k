package com.liangwj.tools2k.apiServer.security;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

public class UrlStatServiceTest {

	@Test
	public void createKey() {
		UrlStatService obj = new UrlStatService();

		obj.add("url", TimeUnit.MILLISECONDS.toNanos(1));
		obj.add("url", TimeUnit.MILLISECONDS.toNanos(19));
		obj.add("url", TimeUnit.MILLISECONDS.toNanos(20));
		obj.add("url", TimeUnit.MILLISECONDS.toNanos(21));
		obj.add("url", TimeUnit.MILLISECONDS.toNanos(400));
		obj.add("url", TimeUnit.MILLISECONDS.toNanos(10000));
	}
}
