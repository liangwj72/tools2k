package com.liangwj.tools2k.mail.utils;

/**
 * 收邮件的参数
 */
public class ReceiveMailParams {
	private String host;
	private int port = 995;
	private String user;
	private String password;

	/** 只收新邮件，这个UUID以前的就不要了（不包含） */
	private String forderName = "INBOX";

	public String getHost() {
		return host;
	}

	public void setHost(String pop3Host) {
		this.host = pop3Host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int pop3Port) {
		this.port = pop3Port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getForderName() {
		return forderName;
	}

	public void setForderName(String forderName) {
		this.forderName = forderName;
	}
}
