package com.liangwj.tools2k.apiServer.errorPage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import com.alibaba.fastjson.JSON;
import com.liangwj.tools2k.apiServer.beans.CommonErrorResponse;
import com.liangwj.tools2k.apiServer.debugMode.DebugModeProperties;
import com.liangwj.tools2k.apiServer.utils.ExceptionUtil;
import com.liangwj.tools2k.utils.other.LogUtil;

import freemarker.template.TemplateException;

/**
 * 通用的错误处理器，自带模板
 *
 * <pre>
 * 重写BasicErrorController,主要负责系统的异常页面的处理以及错误信息的显示
 * </pre>
 *
 * @author rock 2016年8月24日
 */
@Controller
public class ErrorPageController extends AbstractErrorController {

	/** 传给错误页面的VO的名字 */
	public final static String ERROR_INFO_ATTR_NAME = "errorInfo";

	public final static String ERROR_PATH = "/error";

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ErrorPageController.class);

	@Autowired
	private ErrorPageTemplateService templateService;

	@Autowired
	private ErrorPageProperties prop;

	@Autowired
	private DebugModeProperties debugModeProperties;

	private final ErrorAttributes errorAttributes;

	public ErrorPageController(ErrorAttributes errorAttributes) {
		super(errorAttributes);
		this.errorAttributes = errorAttributes;
	}

	/**
	 * 处理错误
	 *
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return
	 */
	@RequestMapping(ERROR_PATH)
	@ResponseBody
	public String error(HttpServletRequest request, HttpServletResponse response) {

		try {
			final ErrorInfoBean errorInfo = this.getErrorInfo(request);

			log.error("在请求 {} 时，发生了错误， 状态码:{}, 错误信息:{}", errorInfo.getRequestUri(), errorInfo.getStatusCode(),
					errorInfo.getMessage());

			final boolean isAjax = this.isAjaxRequest(request);
			if (isAjax) {
				// 如果是 ajax 请求，就返回json的响应
				return this.getErrorAjaxResponse(response, errorInfo);
			} else {
				if (errorInfo.isApiException()) {
					// API错误不视为500错误
					response.setStatus(errorInfo.getStatusCode());
				} else {
					response.setStatus(errorInfo.getStatusCode());
				}

				// 如果是页面，就根据模板返回不同的页面
				return this.getErrorPage(errorInfo);
			}
		} catch (final Throwable ex) {
			log.error("吐血了，生成错误页面的时候出RuntimeException, 导致错误页面没法正常显示", ex);
			return "出错了";

		}
	}

	/**
	 * 根据状态码，返回不同的错误页面
	 *
	 * @param errorInfo
	 *            errorInfo
	 * @return 页面内容
	 * @throws IOException
	 * @throws TemplateException
	 */
	private String getErrorPage(ErrorInfoBean errorInfo) throws TemplateException, IOException {
		final Map<String, Object> model = new HashMap<>();
		model.put(ERROR_INFO_ATTR_NAME, errorInfo);

		final String templatePath = this.prop.getTemplatePath(errorInfo.getStatusCode());
		return this.templateService.process(templatePath, model);
	}

	/**
	 * 根据创痛码，返回不同的json信息
	 *
	 * @param errorInfo
	 *            errorInfo
	 * @return json信息
	 */
	private String getErrorAjaxResponse(HttpServletResponse response, ErrorInfoBean errorInfo) {

		CommonErrorResponse res;

		response.setStatus(errorInfo.getStatusCode());

		// 如果是Ajax请求
		switch (errorInfo.getStatusCode()) {
		case 404:
			// 如果是404错误，就将url返回过去
			res = new CommonErrorResponse(errorInfo.getRequestUri());
			break;
		case 405:
			// 如果是405错误，就将url返回过去
			res = new CommonErrorResponse(errorInfo.getRequestUri());

			break;

		default:
			// 正常的500错误
			res = ExceptionUtil.getErrorResponse(response, errorInfo.getException(),
					this.debugModeProperties.isDebugMode());
			break;
		}

		if (this.debugModeProperties.isDebugMode()) {
			// 如果是开发模式，将调查错误过程回馈给客户端
			if (errorInfo.getException() != null) {
				// 如果有错误，就返回调用过程
				res.setMessage(LogUtil.getTraceString(null, errorInfo.getException()));
			} else {
				// 如果没有错误，就返回错误信息
				res.setMessage(errorInfo.getMessage());
			}
		}

		return JSON.toJSONString(res, true);
	}

	/**
	 * 根据 HttpServletRequest，提取错误信息
	 *
	 * @param request
	 *            HttpServletRequest
	 * @return 错误信息
	 */
	private ErrorInfoBean getErrorInfo(HttpServletRequest request) {

		if (log.isDebugEnabled()) {
			final Map<String, Object> errors = this.getErrorAttributes(request,
					ErrorAttributeOptions.of(Include.STACK_TRACE));

			log.debug("错误信息:{}", errors);
		}

		final RequestAttributes requestAttributes = new ServletRequestAttributes(request);

		final Throwable ex = this.errorAttributes.getError(new ServletWebRequest(request));
		final String requestUri = this.getAttribute(requestAttributes, "javax.servlet.error.request_uri");
		final int status = this.getStatus(request).value();

		final ErrorInfoBean bean = new ErrorInfoBean(requestUri, status, ex);
		bean.setDebugMode(this.debugModeProperties.isDebugMode());
		return bean;
	}

	@SuppressWarnings("unchecked")
	private <T> T getAttribute(RequestAttributes requestAttributes, String name) {
		return (T) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
	}

	// @Override
	// public String getErrorPath() {
	// return ERROR_PATH;
	// }

	/**
	 * 我们通过 request的 header判断，而不是用 {@link RequestMapping#produces()}, 因为如果ajax
	 * dataType 是html时，{@link RequestMapping#produces()} 会视为页面请求
	 *
	 * @param request
	 *            HttpServletRequest
	 * @return
	 */
	private boolean isAjaxRequest(HttpServletRequest request) {
		final String requestedWith = request.getHeader("X-Requested-With");
		return requestedWith != null ? "XMLHttpRequest".equals(requestedWith) : false;
	}
}
