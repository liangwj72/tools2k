package com.liangwj.tools2k.apiServer.security;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;

import com.liangwj.tools2k.annotation.api.ADomainOrder;
import com.liangwj.tools2k.apiServer.beans.manager.RuntimeInfoBean;
import com.liangwj.tools2k.utils.spring.CommonMBeanDomainNaming;

/**
 * <pre>
 * 用于统计的计数器服务
 * </pre>
 * 
 * @author rock
 * 
 */
@Service
@ManagedResource(description = "系统计数器")
@ADomainOrder(order = CommonMBeanDomainNaming.ORDER, domainName = CommonMBeanDomainNaming.DOMAIN)
public class CounterServiceMBean {

	@Autowired
	private CounterService couterService;

	@ManagedOperation(description = "重置所有计数器")
	public void reset() {
		this.couterService.reset();
	}

	@ManagedAttribute(description = "HTTP: 请求总数")
	public long getActionCount() {
		return this.couterService.getActionCounter().getCount();
	}

	@ManagedAttribute(description = "HTTP: 平均时长-微秒")
	public long getActionAvgTime() {
		return TimeUnit.NANOSECONDS.toMicros(this.couterService.getActionCounter().getTimeAvg());
	}

	@ManagedAttribute(description = "WS下行：平均流量")
	public long getWsDownPayloadAvg() {
		return this.couterService.getWsDownCounter().getPayloadAvg();
	}

	@ManagedAttribute(description = "WS下行：平均耗时-微秒")
	public long getWsDownTimeAvg() {
		return TimeUnit.NANOSECONDS.toMicros(this.couterService.getWsDownCounter().getTimeAvg());
	}

	@ManagedAttribute(description = "WS下行：消息总数")
	public long getWsDownCount() {
		return this.couterService.getWsDownCounter().getCount();
	}

	@ManagedAttribute(description = "WS下行：总流量")
	public long getWsDownPayload() {
		return this.couterService.getWsDownCounter().getPayloadTotal();
	}

	@ManagedAttribute(description = "WS上行请求：平均流量")
	public long getWsUpPayloadAvg() {
		return this.couterService.getWsUpCounter().getPayloadAvg();
	}

	@ManagedAttribute(description = "WS上行请求：平均耗时-微秒")
	public long getWsUpTimeAvg() {
		return TimeUnit.NANOSECONDS.toMicros(this.couterService.getWsUpCounter().getTimeAvg());
	}

	@ManagedAttribute(description = "WS上行请求：请求总数")
	public long getWsUpCount() {
		return this.couterService.getWsUpCounter().getCount();
	}

	@ManagedAttribute(description = "WS上行请求：总流量")
	public long getWsUpPayload() {
		return this.couterService.getWsUpCounter().getPayloadTotal();
	}

	@ManagedAttribute(description = "WS上行请求：最大流量")
	public long getWsUpPayloadMax() {
		return this.couterService.getWsUpCounter().getPayloadMax();
	}

	@ManagedAttribute(description = "WS上行请求：最长耗时-微秒")
	public long getWsUpTimeMax() {
		return TimeUnit.NANOSECONDS.toMicros(this.couterService.getWsUpCounter().getTimeMax());
	}

	@ManagedAttribute(description = "最后的系统状态")
	public RuntimeInfoBean getLastRuntime() {
		return this.couterService.getRuntimeHistory().getLast();
	}

	@ManagedOperation(description = "将当前的系统状态加入到历史状态中")
	public void calcLastRuntimeNot() {
		this.couterService.addCurTimeHistoryPoint();
	}
}
