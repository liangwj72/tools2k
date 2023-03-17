package com.liangwj.tools2k.mail.sender;

import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;

import com.liangwj.tools2k.annotation.api.ADomainOrder;
import com.liangwj.tools2k.utils.spring.CommonMBeanDomainNaming;
import com.liangwj.tools2k.utils.threadPool.BaseThreadPool;

/**
 * <pre>
 * 发邮件的线程池
 * </pre>
 * 
 */
@Service
@ManagedResource(description = "发邮件线程池")
@ADomainOrder(order = CommonMBeanDomainNaming.ORDER, domainName = CommonMBeanDomainNaming.DOMAIN_MAIL)
public class SendMailThreadPool extends BaseThreadPool {

	@Override
	protected String getName() {
		return "发送邮件线程池";
	}

	@Override
	protected int getPoolSize() {
		// 发邮件是非常慢的任务，可以将线程池的数量调大点
		return 5;
	}

	@Override
	protected int getUniqueIdSetInitSize() {
		return 1000;
	}

}
