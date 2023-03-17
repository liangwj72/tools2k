package com.liangwj.tools2k.db.redisCache;

import org.springframework.jmx.export.annotation.ManagedResource;

import com.liangwj.tools2k.annotation.api.ADomainOrder;
import com.liangwj.tools2k.utils.spring.CommonMBeanDomainNaming;
import com.liangwj.tools2k.utils.threadPool.BaseThreadPool;

@ManagedResource(description = "异步Cache线程池")
@ADomainOrder(order = CommonMBeanDomainNaming.ORDER, domainName = CommonMBeanDomainNaming.DOMAIN)
public class RedisCacheThreadPool extends BaseThreadPool {

	@Override
	protected String getName() {
		return "异步Cache线程池";
	}

	@Override
	protected int getPoolSize() {
		return 5;
	}

	@Override
	protected int getUniqueIdSetInitSize() {
		return 0;
	}

}
