package com.liangwj.tools2k.apiServer.beans.manager;

import com.liangwj.tools2k.annotation.api.AComment;

import lombok.Getter;
import lombok.Setter;

@AComment("服务组件的状态")
@Setter
@Getter
public class ServerInfoBean {

	@AComment("框架管理账号是否是在配置文件中配置的")
	private boolean adminInProp;

	@AComment("服务器是否debug模式")
	private boolean debugMode;

	@AComment("是否有WebSocket Api接口")
	private boolean hasWsApiImpl;

	@AComment("是否有Druid监控")
	private boolean hasDruid;

	@AComment("是否有发包数据")
	private boolean hasSendPacketData;

	public boolean isAdminInProp() {
		return adminInProp;
	}

	public void setAdminInProp(boolean adminInProp) {
		this.adminInProp = adminInProp;
	}

	public boolean isDebugMode() {
		return debugMode;
	}

	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}

	public boolean isHasWsApiImpl() {
		return hasWsApiImpl;
	}

	public void setHasWsApiImpl(boolean hasWsApiImpl) {
		this.hasWsApiImpl = hasWsApiImpl;
	}

	public boolean isHasDruid() {
		return hasDruid;
	}

	public void setHasDruid(boolean hasDruid) {
		this.hasDruid = hasDruid;
	}

	public boolean isHasSendPacketData() {
		return hasSendPacketData;
	}

	public void setHasSendPacketData(boolean hasSendPacketData) {
		this.hasSendPacketData = hasSendPacketData;
	}

}
