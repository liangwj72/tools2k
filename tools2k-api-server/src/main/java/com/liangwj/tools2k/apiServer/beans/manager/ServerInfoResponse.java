package com.liangwj.tools2k.apiServer.beans.manager;

import java.util.Map;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.apiServer.ajax.api.beans.CommonAdminUserInfoBean;
import com.liangwj.tools2k.apiServer.beans.BaseResponse;

/**
 * <pre>
 * 服务器信息
 * </pre>
 * 
 * @author rock
 * 
 */
public class ServerInfoResponse extends BaseResponse {

	@AComment("服务器信息")
	private ServerInfoBean serverInfo;

	@AComment("框架管理用户信息")
	private CommonAdminUserInfoBean curUser;

	@AComment("所有的字典数据")
	private Map<String, String> dict;

	public ServerInfoBean getServerInfo() {
		return serverInfo;
	}

	public void setServerInfo(ServerInfoBean serverInfo) {
		this.serverInfo = serverInfo;
	}

	public CommonAdminUserInfoBean getCurUser() {
		return curUser;
	}

	public void setCurUser(CommonAdminUserInfoBean curUser) {
		this.curUser = curUser;
	}

	public Map<String, String> getDict() {
		return dict;
	}

	public void setDict(Map<String, String> dict) {
		this.dict = dict;
	}

}
