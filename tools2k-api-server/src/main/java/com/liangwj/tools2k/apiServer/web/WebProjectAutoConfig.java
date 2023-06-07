package com.liangwj.tools2k.apiServer.web;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.liangwj.tools2k.apiServer.ajax.AjaxApiServerAutoConfig;
import com.liangwj.tools2k.apiServer.dict.DictAutoConfig;
import com.liangwj.tools2k.apiServer.jmxInWeb.JmxInWebAutoConfig;
import com.liangwj.tools2k.apiServer.security.LoginCheckAutoConfig;
import com.liangwj.tools2k.utils.spring.BaseAutoConfig;

import jakarta.annotation.PostConstruct;

/**
 * web项目的自动化配置
 *
 * @author rock
 *
 */
@Configuration
@ComponentScan(basePackageClasses = WebProjectAutoConfig.class)
@Import(value = {
		LoginCheckAutoConfig.class, // loginCheck简易安全框架
		JmxInWebAutoConfig.class, // JMX 页面
		DictAutoConfig.class, // 字典
		JmxAutoConfiguration.class, // jmx
		AjaxApiServerAutoConfig.class, // api 服务
})
public class WebProjectAutoConfig extends BaseAutoConfig implements WebMvcConfigurer {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(WebProjectAutoConfig.class);

	/* 后台各类系统的路径的前缀 */
	public static final String PATH_PREFIX = "/_common_";

	/* 后台 入口 */
	public static final String PATH_ADMIN = PATH_PREFIX + "/admin";

	/* 后台用的静态资源的路径 */
	public static final String PATH_STATICS = PATH_PREFIX + "/statics";

	/* 后台用的静态资源的路径 */
	public static final String PATH_DEV = PATH_PREFIX + "/dev";

	@PostConstruct
	protected void init() {
		log.info("自动配置 - Web 项目服务, \n\t框架后台管理 http://127.0.0.1:{}{}/,", this.getServerPort(), PATH_ADMIN);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 统一管理后台的静态资源
		log.info("自动配置 - Web 项目服务, 建立后台系统静态资源路径 {}  映射", PATH_STATICS);
		registry.addResourceHandler(PATH_STATICS + "/**").addResourceLocations("classpath:/commons_statics/");
		registry.addResourceHandler(PATH_ADMIN + "/**").addResourceLocations("classpath:/commons_statics/admin/");
	}

	@Bean
	@ConditionalOnMissingBean(InternalResourceViewResolver.class)
	public InternalResourceViewResolver defaultViewResolver() {
		return new InternalResourceViewResolver();
	}
}
