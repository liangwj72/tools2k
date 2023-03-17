package com.liangwj.tools2k.apiServer.dict;

import org.springframework.stereotype.Service;

import com.liangwj.tools2k.utils.spring.BaseTemplateService;

/**
 * <pre>
 * Freemarker模板引擎
 * </pre>
 * 
 * @author rock 2016年11月17日
 */
@Service
public class DictTemplateService extends BaseTemplateService {

	@Override
	protected String getTemplatePath(String templateName) {
		return String.format("templates/common_dict/%s.ftl", templateName);
	}

}
