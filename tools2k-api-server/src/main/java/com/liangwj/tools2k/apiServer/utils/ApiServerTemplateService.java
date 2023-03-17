package com.liangwj.tools2k.apiServer.utils;

import com.liangwj.tools2k.utils.spring.BaseTemplateService;

/**
 * <pre>
 * Freemarker模板引擎
 * </pre>
 * 
 */
public class ApiServerTemplateService extends BaseTemplateService {

	private static ApiServerTemplateService instance;

	private ApiServerTemplateService() {
	}

	public synchronized static ApiServerTemplateService getInstance() {
		if (instance == null) {
			instance = new ApiServerTemplateService();
		}
		return instance;
	}

	@Override
	protected String getTemplatePath(String templateName) {
		return String.format("templates/api_server/%s.ftl", templateName);
	}

}
