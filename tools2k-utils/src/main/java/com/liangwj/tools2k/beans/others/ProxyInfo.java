package com.liangwj.tools2k.beans.others;

import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class ProxyInfo {
	private final String ip;
	private final int port;
	private String username;
	private String password;

	private ProxyInfo(String ip, int port) {

		Assert.notNull(ip, "ip must not be null");
		Assert.isTrue(port > 0 && port < 65536, " port 不合法");

		this.ip = ip;
		this.port = port;
	}

	/** 是否有密码 */
	public boolean isHasPassword() {
		return StringUtils.hasText(this.password) && StringUtils.hasText(this.username);
	}

	public ProxyInfo setUsername(String username) {
		this.username = username;
		return this;
	}

	public ProxyInfo setPassword(String password) {
		this.password = password;
		return this;
	}

	public static ProxyInfo create(String ip, int port) {
		return new ProxyInfo(ip, port);
	}

	public PasswordAuthentication getPasswordAuthentication() {
		if (this.isHasPassword()) {
			return new PasswordAuthentication(this.username, this.password.toCharArray());
		} else {
			return null;
		}
	}

	public String getKey() {
		return calcPasswordKey(ip, port);
	}

	public InetSocketAddress getInetSocketAddress() {
		return new InetSocketAddress(ip, port);
	}

	public static String calcPasswordKey(String ip, int port) {
		return ip + ":" + port;
	}
}
