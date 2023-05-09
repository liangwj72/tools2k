package com.liangwj.tools2k.apiServer.websocket;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.liangwj.tools2k.apiServer.ajax.api.beans.DevPageResponse;
import com.liangwj.tools2k.apiServer.utils.BaseApiContainer;
import com.liangwj.tools2k.apiServer.utils.BaseApiDebugController;
import com.liangwj.tools2k.apiServer.web.WebProjectAutoConfig;

import freemarker.template.TemplateException;

/***
 * <pre>
 * *开发调试时用的*web socket 测试*
 * </pre>
 *
 * @author 梁韦江
 */
@RequestMapping(WebProjectAutoConfig.PATH_DEV)
@Controller
public class WsApiDebugController extends BaseApiDebugController {

	@Autowired
	private WsApiMapContainer wsApiMapContainer;

	@RequestMapping("/ws")
	@ResponseBody
	public String devIndex() throws TemplateException, IOException {
		return this.createDevIndexPage();
	}
	@RequestMapping("/wsJson")
	@ResponseBody
	public DevPageResponse devApiJson(HttpServletRequest request) {
		return this.wsApiMapContainer.getDebugPageVo(getApiUtlPrefix());
	}

	@Override
	protected String getTemplateName() {
		return "dev-ws";
	}

	@Override
	protected BaseApiContainer<?> getApiContainer() {
		return this.wsApiMapContainer;
	}

	@Override
	protected String getApiUtlPrefix() {
		return "";
	}

	@Override
	protected void addVoToModel(Map<String, Object> model) {
		model.put("classWs", "class='active'");
		model.put("pageName", "WebSocket Api测试");
	}

}
