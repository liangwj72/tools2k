package com.liangwj.tools2k.db.druid;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties;
import com.alibaba.druid.support.http.WebStatFilter;
import com.liangwj.tools2k.apiServer.ajax.api.CommonPublicImpl;
import com.liangwj.tools2k.utils.spring.BaseAutoConfig;

/**
 * 自动配置：Druid监控
 *
 * @author rock
 *
 */
@Configuration
@ComponentScan(basePackageClasses = MyDruidAutoConfig.class)
@AutoConfigureAfter(DruidDataSourceAutoConfigure.class)
public class MyDruidAutoConfig extends BaseAutoConfig {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MyDruidAutoConfig.class);

	// private static final String DEFAULT_USERNAME = "a";
	// private static final String DEFAULT_PASSWORD = "a";
	// private static final String DEFAULT_ALLOW =
	// "192.168.0.0/16,172.16.0.0/16,127.0.0.1";

	@Bean
	@ConditionalOnMissingBean
	public StatFilter statFilter() {
		CommonPublicImpl.isHasDruid = true;

		log.debug("自动配置 - Druid监控, 默认启动Druid的数据库监控");
		StatFilter res = new StatFilter();
		res.setLogSlowSql(true);
		res.setSlowSqlMillis(1000);
		return res;
	}

	@Bean
	@ConditionalOnMissingBean
	public FilterRegistrationBean<WebStatFilter> webStatFilterRegistrationBean(DruidStatProperties properties) {
		DruidStatProperties.WebStatFilter config = properties.getWebStatFilter();

		String urlPatterns = StringUtils.hasText(config.getUrlPattern()) ? config.getUrlPattern() : "/api/*";

		log.info("自动配置 - Druid监控, 默认启动Druid的web请求监控，只监控{}", urlPatterns);

		FilterRegistrationBean<WebStatFilter> registrationBean = new FilterRegistrationBean<WebStatFilter>();
		WebStatFilter filter = new WebStatFilter();
		registrationBean.setFilter(filter);
		registrationBean
				.addUrlPatterns(urlPatterns);

		registrationBean.addInitParameter("exclusions", config.getExclusions() != null ? config.getExclusions()
				: "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");

		if (config.getSessionStatEnable() != null) {
			registrationBean.addInitParameter("sessionStatEnable", config.getSessionStatEnable());
		}
		if (config.getSessionStatMaxCount() != null) {
			registrationBean.addInitParameter("sessionStatMaxCount", config.getSessionStatMaxCount());
		}
		if (config.getPrincipalSessionName() != null) {
			registrationBean.addInitParameter("principalSessionName", config.getPrincipalSessionName());
		}
		if (config.getPrincipalCookieName() != null) {
			registrationBean.addInitParameter("principalCookieName", config.getPrincipalCookieName());
		}
		if (config.getProfileEnable() != null) {
			registrationBean.addInitParameter("profileEnable", config.getProfileEnable());
		}
		return registrationBean;
	}

}
