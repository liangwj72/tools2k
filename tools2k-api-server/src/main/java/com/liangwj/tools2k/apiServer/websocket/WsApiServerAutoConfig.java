package com.liangwj.tools2k.apiServer.websocket;

import java.nio.charset.Charset;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import com.liangwj.tools2k.apiServer.ajax.AjaxApiServerAutoConfig;

/**
 * <pre>
 * api server 自动化配置websocket
 * 
 * 注意，spring boot的websocket配置和web配置有点冲突，如果存在了websocket配置，就会不调用web项目常规配置，主要缺了以下两样：
 * - 静态资源映射没了，所以另外配置
 * - 自动转码为UTF-8也没有
 * 
 * </pre>
 * 
 * @author 梁韦江 2017年4月14日
 */
@EnableWebMvc
@EnableWebSocket
@Configuration
@ComponentScan(basePackageClasses = WsApiServerAutoConfig.class)
@Import(value = { AjaxApiServerAutoConfig.class, // 基于api server的
})
public class WsApiServerAutoConfig implements WebSocketConfigurer, WebMvcConfigurer {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(WsApiServerAutoConfig.class);

	public WsApiServerAutoConfig() {
		log.debug("自动配置  WsApiServerAutoConfig");
	}

	/** 每个包的大小限制 1M */
	private static final int BUFFER_SIZE_LIMIT = 1000_000;

	@Autowired
	private WebSocketHandlerImpl handler;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		log.debug("静态资源的路径为 /statics/");
		registry.addResourceHandler("/statics/**").addResourceLocations("classpath:/static/statics/");
	}

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		log.debug("强制将所有页面请求返回的编码都修改为utf8");

		final Charset charset = Charset.forName("UTF-8");

		for (final HttpMessageConverter<?> c : converters) {
			if (c instanceof AbstractHttpMessageConverter) {
				((AbstractHttpMessageConverter<?>) c).setDefaultCharset(charset);
			}
		}
	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

		log.debug("注册 WebSocketHandler");

		registry.addHandler(handler, "/wsApi");
		registry.addHandler(handler, "/wsApiSockJs").withSockJS();
	}

	@Bean
	public ServletServerContainerFactoryBean createWebSocketContainer() {
		final ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
		container.setMaxTextMessageBufferSize(BUFFER_SIZE_LIMIT);
		container.setMaxBinaryMessageBufferSize(BUFFER_SIZE_LIMIT);
		// container.setAsyncSendTimeout(100_000_000_000_000L);
		// container.setMaxSessionIdleTimeout(100_000_000_000_000L);
		return container;
	}
}
