package com.liangwj.tools2k.apiServer.beans.manager;

import java.util.Map;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.apiServer.ajax.api.beans.CommonAdminUserInfoBean;
import com.liangwj.tools2k.apiServer.beans.BaseResponse;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 服务器信息
 * </pre>
 * 
 * @author rock
 * 
 */
@Setter
@Getter
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

}
