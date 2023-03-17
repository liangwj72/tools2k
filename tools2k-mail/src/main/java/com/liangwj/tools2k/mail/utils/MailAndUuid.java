package com.liangwj.tools2k.mail.utils;

import java.io.IOException;
import java.util.Date;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 收邮件的结果
 */
public class MailAndUuid {
	private String uuId;
	private MimeMessage message;
	private Address[] addrs;
	private String subject;
	private String content;
	private Date sendDate;

	public MailAndUuid(String uuId, MimeMessage message) {
		super();
		this.uuId = uuId;
		this.message = message;
		if (this.message != null) {
			try {
				this.addrs = message.getFrom();
				this.content = message.getContent().toString();
				this.subject = message.getSubject();
				this.sendDate = message.getSentDate();
			} catch (MessagingException | IOException e) {
			}
		}
	}

	public String getUuId() {
		return uuId;
	}

	public void setUuId(String uuId) {
		this.uuId = uuId;
	}

	public MimeMessage getMessage() {
		return message;
	}

	public void setMessage(MimeMessage message) {
		this.message = message;
	}

	public Address[] getAddrs() {
		return addrs;
	}

	public String getSubject() {
		return subject;
	}

	public String getFrom0() {
		if (this.addrs != null && addrs.length > 0) {
			Address addr = this.addrs[0];
			if (addr instanceof InternetAddress) {
				return ((InternetAddress) addr).getAddress();
			}
		}
		return null;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public String getContent() {
		return this.content;
	}
}
