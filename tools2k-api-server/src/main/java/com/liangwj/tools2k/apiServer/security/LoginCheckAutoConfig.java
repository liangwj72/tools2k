package com.liangwj.tools2k.apiServer.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.session.web.http.SessionRepositoryFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.liangwj.tools2k.apiServer.debugMode.DebugModeAutoConfig;
import com.liangwj.tools2k.apiServer.errorPage.ErrorPageAutoConfig;

/**
 * <pre>
 * {@link LoginCheckInterceptor}的自动化配置
 * 为了满足ajax跨域请求，我们需要实现能从header中获取sessionId，所以我们用了spring session项目
 * 
 * </pre>
 * 
 * @author rock 2016年8月26日
 * 
 * @see SessionRepositoryFilter
 * 
 */
@Configuration
//@EnableSpringHttpSession
@EnableConfigurationProperties(LoginCheckProperties.class)
@ComponentScan(basePackageClasses = LoginCheckAutoConfig.class)
@Import(value = { DebugModeAutoConfig.class, // debug mode组件，用于设置调试模式
		ErrorPageAutoConfig.class, // 错误页面处理
})
public class LoginCheckAutoConfig implements WebMvcConfigurer {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LoginCheckAutoConfig.class);

	/**
	 * 默认的session时间（单位秒）：1天
	 */
	public static final int SESSION_MAX_INACTIVE_INTERVAL_SECONDS = 86400;

	/** 我们用map来保存session，所以要限制一下map的大小 */
	public static final int SESSION_MAP_LIMIT = 10000;

	public LoginCheckAutoConfig() {
		log.debug("自动配置 LoginCheckAutoConfig");
	}

	/**
	 * 创建 LoginCheckInterceptor
	 * 
	 * @return
	 */
	@Autowired
	private LoginCheckInterceptor loginCheckInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		log.debug("注入  LoginCheckInterceptor 到mvc中");

		registry.addInterceptor(this.loginCheckInterceptor);
	}

//	@Bean
	public HeaderAndCookieHttpSessionIdResolver headerAndCookieHttpSessionIdResolver() {
		return new HeaderAndCookieHttpSessionIdResolver();
	}

}
