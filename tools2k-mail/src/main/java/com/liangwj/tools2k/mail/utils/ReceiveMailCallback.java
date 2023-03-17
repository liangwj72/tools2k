package com.liangwj.tools2k.mail.utils;

/**
 * 收到邮件的回调函数
 */
public interface ReceiveMailCallback {
	/**
	 * 收到邮件
	 * 
	 * @param uuId
	 *            邮件的UUID
	 * @param message
	 *            邮件
	 * @return 如果返回真，表示继续收下一封
	 */
	boolean onMail(MailAndUuid mail);
}
