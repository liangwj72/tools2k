package com.liangwj.tools2k.utils.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;

/** 自动化配置的基类 */
public abstract class BaseAutoConfig {

	@Autowired
	protected ServerProperties serverProperties;

	protected int getServerPort() {
		Integer port = this.serverProperties.getPort();
		if (port == null) {
			return 8080;
		} else {
			return port;
		}
	}
}
