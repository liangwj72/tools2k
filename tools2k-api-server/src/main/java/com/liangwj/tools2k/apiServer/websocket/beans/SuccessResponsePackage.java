package com.liangwj.tools2k.apiServer.websocket.beans;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.apiServer.beans.BaseSocketResponse;

/**
 * <pre>
 * 调用接口成功时的返回
 * </pre>
 * 
 * @author 梁韦江
 * 
 */
public class SuccessResponsePackage extends BaseResponsePackage {

	@AComment("接口调用的返回数据")
	private final BaseSocketResponse data;

	public SuccessResponsePackage(CmdBean cmd, BaseSocketResponse data) {
		super(ResponseTypeEnum.Sccess, cmd);
		this.data = data;
	}

	public BaseSocketResponse getData() {
		return data;
	}

}
