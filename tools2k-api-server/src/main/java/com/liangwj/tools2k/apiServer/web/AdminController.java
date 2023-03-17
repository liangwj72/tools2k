package com.liangwj.tools2k.apiServer.web;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import freemarker.template.TemplateException;

/**
 * <pre>
 * 后台controller
 * </pre>
 *
 * @author rock 2016年7月4日
 */
@Controller
public class AdminController {

	@RequestMapping(value = {
			WebProjectAutoConfig.PATH_ADMIN
	})
	public String index1() throws TemplateException, IOException {
		return "redirect:" + WebProjectAutoConfig.PATH_ADMIN + "/";
	}

	@RequestMapping(value = {
			WebProjectAutoConfig.PATH_ADMIN + "/"
	})
	public String index2() throws TemplateException, IOException {
		return "index.html";
	}

}
