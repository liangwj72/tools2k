package com.liangwj.tools2k.apiServer.jmxInWeb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <pre>
 * 旧的界面没用了，重定向到新的
 * </pre>
 * 
 * @author rock
 */
@Controller
public class JwRedirectController {

	@RequestMapping(value = { "jmxInWeb/", "jmxInWeb/index" })
	public String index() {
		return "redirect:/commonAdmin/";
	}
}
