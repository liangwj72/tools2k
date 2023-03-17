package com.liangwj.tools2k.apiServer.beans.manager;

import com.liangwj.tools2k.annotation.api.AComment;

/**
 * <pre>
 * 连接管理器对外提供的信息
 * </pre>
 * 
 * @author rock
 * 
 */
public interface IConnHandlerInfo {

	@AComment("连接时间")
	public long getConnectTime();

	@AComment("下行流量统计")
	public PayloadCounter getDownCounter();

	@AComment("ip地址")
	public String getIpAddress();

	@AComment("获取最后一次请求的时间")
	public long getLastRequestTime();

	@AComment("连接的sessionId")
	public String getSessionId();

	@AComment("上行流量统计")
	public PayloadCounter getUpCounter();

	@AComment("登陆的用户账号")
	public String getUserAccount();

	@AComment("登录的用户类型")
	public Class<?> getUserClass();

	public long getLastUpCountDiff();
}
