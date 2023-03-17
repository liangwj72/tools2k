package com.liangwj.tools2k.apiServer.ajax;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.liangwj.tools2k.apiServer.security.HeaderAndCookieHttpSessionIdResolver;
import com.liangwj.tools2k.apiServer.security.LoginCheckAutoConfig;

/**
 * <pre>
 * api server 自动化配置
 * </pre>
 *
 * @author rock 2017年4月14日
 */
@Configuration
@ComponentScan(basePackageClasses = AjaxApiServerAutoConfig.class)
@Import(value = {
		LoginCheckAutoConfig.class, // loginCheck简易安全框架
})
public class AjaxApiServerAutoConfig implements WebMvcConfigurer {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AjaxApiServerAutoConfig.class);

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		log.info("注册url:/api/** , 并允许跨域访问");
		registry.addMapping("/api/**")
				.allowedOriginPatterns("*")
				.allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS").allowedHeaders("*")
				.exposedHeaders("Set-Cookie").exposedHeaders(HeaderAndCookieHttpSessionIdResolver.HEADER_NAME)
				.allowCredentials(true).maxAge(3600);
	}
}
