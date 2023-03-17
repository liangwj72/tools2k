package com.liangwj.tools2k.apiServer.ajax;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liangwj.tools2k.apiServer.beans.BaseResponse;
import com.liangwj.tools2k.apiServer.utils.ApiMethodInfo;
import com.liangwj.tools2k.beans.exceptions.BaseApiException;

/**
 * <pre>
 * ApiController 的安全服务接口。
 * 如果spring能找到这个接口的服务，就用这个服务作为安全提供者，否则使用默认的
 * </pre>
 * 
 * @author rock 2017年4月22日
 */
public interface IAjaxApiControllerSecurityService {

	/**
	 * 在api server 的对应方法被执行前，先执行这个方法。
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param apiMethodInfo
	 *            准备调用的api的方法
	 * @throws BaseApiException
	 *             如果抛错误，就不会执行apiMethodInfo
	 */
	void onBeforeInvoke(HttpServletRequest request, HttpServletResponse response,
			ApiMethodInfo<BaseResponse> apiMethodInfo)
			throws BaseApiException;

}
