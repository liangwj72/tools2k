package com.liangwj.tools2k.apiServer.dict;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.liangwj.tools2k.apiServer.loginCheck.ANeedCheckLogin;
import com.liangwj.tools2k.apiServer.security.CommonAdminWebUser;
import com.liangwj.tools2k.utils.other.FileUtil;

/**
 * <pre>
 * 导出xml
 * </pre>
 * 
 * @author rock 2016年11月17日
 */
@Controller
public class DictController {

	@Autowired
	private DictProperties prop;

	@Autowired
	private DictCoreService coreService;

	@RequestMapping(value = DictProperties.EXPORT_URL)
	@ResponseBody
	@ANeedCheckLogin(userClass = CommonAdminWebUser.class)
	public String export() throws IOException {

		// 导出前，先保存一次文件
		this.coreService.doSaveAll();

		// xml文件路径
		File file = new File(this.prop.getXmlFullPath());

		return FileUtil.readFile(file);
	}

	@RequestMapping(value = DictProperties.MANAGER_URL)
	public String redirect() {
		return "redirect:/commonAdmin/";
	}
}
