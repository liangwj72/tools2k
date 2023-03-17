package com.liangwj.tools2k.mail.utils;

/**
 * 收到邮件的回调函数
 */
public interface ReceiveMailCallback {
	/**
	 * 收到邮件
	 * 
	 * @param mail
	 *            邮件的UUID和内容
	 * @return 如果返回真，表示继续收下一封
	 */
	boolean onMail(MailAndUuid mail);
}
