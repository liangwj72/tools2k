package com.liangwj.tools2k.apiServer.ajax.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.liangwj.tools2k.apiServer.ajax.AjaxApiMapContainer;
import com.liangwj.tools2k.apiServer.ajax.api.beans.DevPageResponse;
import com.liangwj.tools2k.apiServer.utils.BaseApiContainer;
import com.liangwj.tools2k.apiServer.utils.BaseApiDebugController;
import com.liangwj.tools2k.apiServer.web.WebProjectAutoConfig;
import com.liangwj.tools2k.utils.other.EncodeUtil;
import com.liangwj.tools2k.utils.web.WebUtils;

import freemarker.template.TemplateException;

/**
 * <pre>
 * 开发时调试用的的controller
 * </pre>
 *
 * @author rock 2016年7月4日
 */
@RequestMapping(WebProjectAutoConfig.PATH_DEV)
@Controller
public class AjaxApiDebugController extends BaseApiDebugController {

	private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AjaxApiMapContainer apiMapContainer;

	@RequestMapping({"/",""})
	@ResponseBody
	public String devIndex() throws TemplateException, IOException {

		return this.createDevIndexPage();
	}

	@RequestMapping("/ajaxJson")
	@ResponseBody
	public DevPageResponse devApiJson(HttpServletRequest request) {
		return this.apiMapContainer.getDebugPageVo(getApiUtlPrefix());
	}

	@RequestMapping("/headers")
	@ResponseBody
	public String printHeader(HttpServletRequest request) {

		// 获取所有的header
		final List<String> list = new LinkedList<>();
		final Enumeration<String> e = request.getHeaderNames();
		while (e.hasMoreElements()) {
			final String name = e.nextElement();

			list.add(name + ":\t" + request.getHeader(name));
		}

		// 排序
		Collections.sort(list);

		final StringBuffer sb = new StringBuffer();
		sb.append("来源ip:").append(WebUtils.findRealRemoteAddr(request));

		for (final String str : list) {
			sb.append("\n");
			sb.append(str);
		}

		if (log.isDebugEnabled()) {
			log.debug(sb.toString());
		}

		return EncodeUtil.html(sb.toString(), false);
	}

	@Override
	protected BaseApiContainer<?> getApiContainer() {
		return this.apiMapContainer;
	}

	@Override
	protected String getApiUtlPrefix() {
		return "/api";
	}

	@Override
	protected String getTemplateName() {
		return "dev-ajax";
	}

	@Override
	protected void addVoToModel(Map<String, Object> model) {
		model.put("classAjax", "class='active'");
		model.put("pageName", "Ajax Api测试");
	}
}
