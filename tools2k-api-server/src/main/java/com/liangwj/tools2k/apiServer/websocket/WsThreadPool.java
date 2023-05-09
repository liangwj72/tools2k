package com.liangwj.tools2k.apiServer.websocket;

import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;

import com.liangwj.tools2k.annotation.api.ADomainOrder;
import com.liangwj.tools2k.utils.spring.CommonMBeanDomainNaming;
import com.liangwj.tools2k.utils.threadPool.BaseThreadPool;

/**
 * <pre>
 * 线程池
 * </pre>
 * 
 * @author 江凯文
 * 
 */
@Service
@ManagedResource(description = "Websocket Api线程池")
@ADomainOrder(domainName = CommonMBeanDomainNaming.DOMAIN_API_SERVER, order = CommonMBeanDomainNaming.ORDER)
public class WsThreadPool extends BaseThreadPool {

	@Override
	protected String getName() {
		return "websock异步线程池";
	}

	@Override
	protected int getUniqueIdSetInitSize() {
		// 任务排重缓冲区的容量，用于防止重复名字的任务，但超过1000个任务时，后面的就没法排重了
		return 1000;
	}

}
