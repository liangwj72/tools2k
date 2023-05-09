package com.liangwj.tools2k.apiServer.websocket;

import com.liangwj.tools2k.apiServer.beans.BaseSocketResponse;
import com.liangwj.tools2k.apiServer.utils.ApiMethodInfo;
import com.liangwj.tools2k.apiServer.websocket.WsLoginContext.ConnHandler;
import com.liangwj.tools2k.beans.exceptions.BaseApiException;

/**
 * <pre>
 * ws接口默认额外的服务器，用于在各个事件中执行额外的逻辑
 * </pre>
 * 
 * @author 梁韦江
 */
public interface IWsExtService {

	/**
	 * 对应方法被执行前，先执行这个方法。
	 * 
	 * @param handler
	 *            连接信息
	 * @param apiMethodInfo
	 *            准备调用的api的方法
	 * @throws BaseApiException
	 *             如果抛错误，就不会执行apiMethodInfo
	 */
	void onBeforeInvokeApi(ConnHandler handler, ApiMethodInfo<BaseSocketResponse> apiMethodInfo)
			throws BaseApiException;

	/**
	 * 用户断线时触发，在执行WsLoginContext.afterConnectionClosed 前执行
	 * 
	 * @param handler
	 * @throws BaseApiException
	 */
	void afterConnectionClosed(ConnHandler handler) throws BaseApiException;
}
