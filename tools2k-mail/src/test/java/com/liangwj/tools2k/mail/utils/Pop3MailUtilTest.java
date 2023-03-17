package com.liangwj.tools2k.mail.utils;

import javax.mail.MessagingException;

import org.junit.jupiter.api.Test;

public class Pop3MailUtilTest {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Pop3MailUtilTest.class);

	@Test
	public void testReceive() throws MessagingException {

		ReceiveMailParams p = new ReceiveMailParams();
		p.setHost("pop-mail.outlook.com");
		p.setUser("邮箱");
		p.setPassword("密码");

		ReceiveMailCallback callBack = new ReceiveMailCallback() {

			@Override
			public boolean  onMail(MailAndUuid mail) {
				try {
					debugMail(mail);
				} catch (MessagingException e) {
				}
				return false;
			}
		};
		
		Pop3MailUtil.receiveEmail(p,callBack);
	}

	private void debugMail(MailAndUuid mail) throws MessagingException {
		mail.getMessage();
		log.debug("Uid:{}, 标题:{}, 收件人:{} 时间:{}  -- {}",
				mail.getUuId(), mail.getSubject(), mail.getFrom0(),
				mail.getMessage().getSentDate()
		// , mail.getContent()
				);
	}

}
