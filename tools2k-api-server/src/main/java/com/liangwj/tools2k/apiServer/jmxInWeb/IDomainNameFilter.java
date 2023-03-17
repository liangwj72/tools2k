package com.liangwj.tools2k.apiServer.jmxInWeb;

/**
 * <pre>
 * MBean Domain的过滤器，有时我们并不希望所有的Domain都显示出来
 * </pre>
 * 
 * @author rock 2016年6月7日
 */
public interface IDomainNameFilter {

	boolean show(String domainName);

}
