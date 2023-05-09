package com.liangwj.tools2k.apiServer.websocket.beans;

import java.util.Map;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.apiServer.utils.ExceptionUtil;
import com.liangwj.tools2k.beans.exceptions.BaseApiException;

/**
 * <pre>
 * 调用接口 失败时的返回
 * </pre>
 * 
 * @author 梁韦江
 * 
 */
public class ErrorResponsePackage extends BaseResponsePackage {

	@AComment("错误代码")
	private final String errorCode;

	@AComment("错误消息")
	private final String errorMsg;

	@AComment("错误额外的数据")
	private final Map<String, Object> errorData;

	public ErrorResponsePackage(CmdBean cmd, Throwable e) {
		super(ResponseTypeEnum.Sccess, cmd);

		this.errorCode = e.getClass().getSimpleName();

		if (e instanceof BaseApiException) {

			BaseApiException e1 = (BaseApiException) e;
			this.errorMsg = e1.getErrorMsg();

			// 同时设置额外的数据进去
			this.errorData = ExceptionUtil.getExDataFromApiException(e1);

		} else {
			// 其他错误时，返回系统错误
			this.errorMsg = e.getMessage();
			this.errorData = null;
		}

	}

	public Map<String, Object> getErrorData() {
		return errorData;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

}
