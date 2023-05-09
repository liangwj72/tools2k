package com.liangwj.tools2k.apiServer.websocket.beans;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.apiServer.beans.BaseSocketPackage;

/**
 * <pre>
 * 对返回内容的封装
 * </pre>
 * 
 * @author 梁韦江
 * 
 */
public abstract class BaseResponsePackage extends BaseSocketPackage {

	@AComment("接口url")
	private final String url;

	@AComment("客户端传过来的序列号")
	private final String sid;

	public BaseResponsePackage(ResponseTypeEnum msgType, CmdBean cmd) {
		super(msgType);
		this.url = cmd.getUrl();
		this.sid = cmd.getSid();
	}

	public String getUrl() {
		return url;
	}

	public String getSid() {
		return sid;
	}

}
