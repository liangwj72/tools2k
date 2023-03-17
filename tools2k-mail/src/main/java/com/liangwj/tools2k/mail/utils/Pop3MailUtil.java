package com.liangwj.tools2k.mail.utils;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.springframework.util.Assert;

import com.sun.mail.pop3.POP3Folder;
import com.sun.mail.pop3.POP3Store;

/**
 * POP3收邮件工具
 */
public class Pop3MailUtil {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Pop3MailUtil.class);
	private static String STORE_TYPE = "pop3";

	/**
	 * 收邮件
	 * 
	 * @param params
	 *            邮件服务器的参数
	 * @param callBack
	 *            回调函数，注意是先给最新的邮件
	 * @throws MessagingException
	 */
	public static void receiveEmail(ReceiveMailParams params, ReceiveMailCallback callBack) throws MessagingException {

		Assert.notNull(callBack, "callBack must not be null");

		POP3Store emailStore = null;
		POP3Folder emailFolder = null;
		try {
			Properties props = System.getProperties();

			props.setProperty("mail.pop3.host", params.getHost());
			props.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.setProperty("mail.pop3.socketFactory.fallback", "false");
			props.setProperty("mail.pop3.port", String.valueOf(params.getPort()));
			props.setProperty("mail.pop3.socketFactory.port", String.valueOf(params.getPort()));
			props.setProperty("mail.pop3.auth", "true");

			// 创建 session
			Session emailSession = Session.getDefaultInstance(props);

			// 连接服务器
			emailStore = (POP3Store) emailSession.getStore(STORE_TYPE);
			emailStore.connect(params.getUser(), params.getPassword());
			log.debug("连接POP3服务器 {}", params.getHost());

			// 打开收件箱
			emailFolder = (POP3Folder) emailStore.getFolder(params.getForderName());
			emailFolder.open(Folder.READ_ONLY);
			log.debug("打开收件箱:{}", params.getForderName());

			// 收邮件
			Message[] messages = emailFolder.getMessages();
			log.debug("总共有 {} 封邮件", messages.length);

			// 从最新的开始收
			for (int i = messages.length - 1; i >= 0; i--) {

				MimeMessage message = (MimeMessage) messages[i];
				String uid = emailFolder.getUID(message);

				boolean ok = callBack.onMail(new MailAndUuid(uid, message));
				if (!ok) {
					log.debug("邮件接受回调函数要求不再收件");
					// 如果回调返回说不继续收件了，就退出循环
					break;
				}
			}

		} finally {
			if (emailFolder != null) {
				try {
					emailFolder.close();
				} catch (MessagingException e) {
				}
			}
			if (emailStore != null) {
				try {
					emailStore.close();
				} catch (MessagingException e) {
				}
			}
		}
	}
}
