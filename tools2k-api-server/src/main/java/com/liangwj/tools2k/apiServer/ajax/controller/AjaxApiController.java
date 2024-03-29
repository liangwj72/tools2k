package com.liangwj.tools2k.apiServer.ajax.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.liangwj.tools2k.apiServer.ajax.AjaxApiMapContainer;
import com.liangwj.tools2k.apiServer.ajax.IAjaxApiControllerSecurityService;
import com.liangwj.tools2k.apiServer.beans.BaseResponse;
import com.liangwj.tools2k.apiServer.beans.CommonSuccessResponse;
import com.liangwj.tools2k.apiServer.security.LoginContext;
import com.liangwj.tools2k.apiServer.utils.ApiMethodInfo;
import com.liangwj.tools2k.apiServer.utils.ExceptionUtil;
import com.liangwj.tools2k.beans.exceptions.ApiNotFoundException;
import com.liangwj.tools2k.beans.exceptions.BaseApiException;
import com.liangwj.tools2k.utils.web.BinderUtil;
import com.liangwj.tools2k.utils.web.WebUtils;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <pre>
 * Ajax接入方式的基类，用的是spring mvc
 * 
 * 带有自动判断JSONP的功能。
 * 
 * </pre>
 * 
 * @author rock 2016年6月30日
 */
@RestController
@RequestMapping("/api")
public class AjaxApiController {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AjaxApiController.class);

	@Autowired
	private AjaxApiMapContainer apiMapContainer;

	@Autowired(required = false)
	private LoginContext loginContext;

	/** 额外的安全服务，如果能找到，就用这个作为安全服务，否则就直接用loginContext */
	@Autowired(required = false)
	private IAjaxApiControllerSecurityService securityService;

	/**
	 * 用于自动判断是否是jsonp的情况
	 */
	protected String getJsonpParamName() {
		return "callback";
	}

	@PostConstruct
	protected void init() {
		if (this.securityService != null) {
			log.info("{} 有特殊的安全服务者 {}", this.getClass().getSimpleName(),
					this.securityService.getClass().getSimpleName());
		} else {
			if (this.loginContext == null) {
				log.warn("警告！！ {} 没有任何安全保障，请检查");
			}
		}
	}

	/**
	 * 进入invoke时，先执行一个钩子方法，让子类有更过的空间增加功能
	 * 
	 * @param req
	 *            req
	 * @param resp
	 *            resp
	 * @param methodInfo
	 *            ApiMethodInfo
	 */
	private void onBeforeInvoke(HttpServletRequest req, HttpServletResponse resp,
			ApiMethodInfo<BaseResponse> methodInfo) throws BaseApiException {

		if (methodInfo.isNeedLogin()) {
			if (this.securityService != null) {
				this.securityService.onBeforeInvoke(req, resp, methodInfo);
			} else {
				// 否则就用LoginCheck的安全检查
				methodInfo.checkRights(this.loginContext);
			}
		}
	}

	@RequestMapping(value = "/{infName}/{methodName}")
	@ResponseBody
	public String invoke(@PathVariable String infName, @PathVariable String methodName, HttpServletRequest req,
			HttpServletResponse resp) {

		/** Ajax跨域header */
//		WebUtils.addCrossDomainHeader(resp, req);

		ApiMethodInfo<BaseResponse> methodInfo = this.apiMapContainer.findApiMethod(infName, methodName);

		BaseResponse res = null;
		Object param = null;

		long start = System.currentTimeMillis();
		try {
			try {
				if (methodInfo == null) {
					// 找不到api就直接抛错
					throw new ApiNotFoundException(infName, methodName);
				}

				if (log.isDebugEnabled()) {
					StringBuffer sb = new StringBuffer();
					WebUtils.debugRequest(req, sb);

					log.debug("准备调用接口: {}/{} \n\tRequest中的信息:{}", infName, methodName, sb.toString());
				}

				// 进入方法时，先执行一个方法，看看是否要处理一下HttpRequest的东西
				this.onBeforeInvoke(req, resp, methodInfo);

				if (methodInfo.hasParam()) {
					// 如果该接口有参数，就从http请求绑定参数
					param = BinderUtil.bindForm(req, methodInfo.getFormClass(), false);
				}

				// 执行接口方法
				res = methodInfo.invoke(param);

			} catch (Exception e) {
				res = ExceptionUtil.getErrorResponse(resp, e, log.isDebugEnabled());
			}

			return this.detectJsonp(res, req);
		} finally {

			long end = System.currentTimeMillis();

			if (log.isDebugEnabled()) {
				String paramStr = null;
				if (param != null && !methodInfo.isUploadFile()) {
					// 如果接口有上传文件，不能随便将表单 toJsonString，如果上传个1M的文件...
					paramStr = JSON.toJSONString(param, true);
				}

				String responseStr = res == null ? null : JSON.toJSONString(res, true);
				log.debug("调用接口: {}/{} 时间消耗 :{}ms \n绑定表单的结果:{} \n返回内容:{}", infName, methodName, end - start, paramStr,
						responseStr);
			}
		}
	}

	private String detectJsonp(BaseResponse res, HttpServletRequest request) {
		if (res == null) {
			res = new CommonSuccessResponse();
		}

		String body = JSON.toJSONString(res, true);
		// 获取jsonp callback的函数名
		String callbackName = request.getParameter(getJsonpParamName());

		if (StringUtils.hasText(callbackName)) {
			// 如果是 jsonp的请求，就包装一下
			return String.format("%s(%s);", callbackName, body);
		} else {
			// 如果不是jsonp,就直接返回内容
			return body;
		}
	}

}
