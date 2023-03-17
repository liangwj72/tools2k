package com.liangwj.tools2k.apiServer.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.liangwj.tools2k.apiServer.ajax.AjaxApiMapContainer;
import com.liangwj.tools2k.apiServer.ajax.api.beans.DevPageResponse;
import com.liangwj.tools2k.apiServer.debugMode.DebugModeProperties;
import com.liangwj.tools2k.apiServer.serviceInf.IDictService;
import com.liangwj.tools2k.apiServer.serviceInf.IWsApiServer;

import freemarker.template.TemplateException;

/***
 * <pre>
 * *开发调试时用的*web socket 测试*
 * </pre>
 * 
 * @author rock
 */
public abstract class BaseApiDebugController {

	@Autowired
	private DebugModeProperties debugMode;

	@Autowired(required = false)
	private IDictService dictCoreService;

	@Autowired(required = false)
	private IWsApiServer webSocketImpl;

	@Autowired(required = false)
	private AjaxApiMapContainer ajaxImpl;
	
	protected String createDevIndexPage() throws TemplateException, IOException {

		Map<String, Object> model = new HashMap<>();

		// 调试页面的vo
		DevPageResponse vo = this.getApiContainer().getDebugPageVo(this.getApiUtlPrefix());
		model.put("vo", vo); // 根据反射接口实现类生成的 页面vo

		model.put("hasWs", this.webSocketImpl != null); // 是否有ws接口
		model.put("hasAjax", this.ajaxImpl != null); // 是否有ajax接口

		this.addVoToModel(model);

		if (this.dictCoreService != null) {
			model.put("pageTitle", this.dictCoreService.getSystemName());
		}

		if (this.debugMode.isDebugMode()) {
			return ApiServerTemplateService.getInstance().process(this.getTemplateName(), model);
		} else {
			return "isDebugMode=false";
		}
	}

	protected abstract void addVoToModel(Map<String, Object> model);

	protected abstract String getApiUtlPrefix();

	protected abstract String getTemplateName();

	protected abstract BaseApiContainer<?> getApiContainer();
}
