package com.liangwj.tools2k.apiServer.jmxInWeb;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.liangwj.tools2k.apiServer.security.LoginCheckAutoConfig;

/**
 * <pre>
 * 自动配置jmx的web管理界面
 * 
 * </pre>
 * 
 * @see JmxInWebService 配置后可以使用该服务注册和反注册mbean
 * 
 * @author rock 2016年8月11日
 */
@Configuration
@ComponentScan(basePackageClasses = JmxInWebAutoConfig.class)
@EnableConfigurationProperties(JmxInWebProperties.class)
@Import(value = {
		LoginCheckAutoConfig.class, // loginCheck简易安全框架，包含debugMode和错误页面处理
})
public class JmxInWebAutoConfig {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(JmxInWebAutoConfig.class);

	public JmxInWebAutoConfig() {
		log.info("自动配置 - JmxInWeb，可通过页面访问jmx");
	}
}
