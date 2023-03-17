package com.liangwj.tools2k.apiServer.dict;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.liangwj.tools2k.apiServer.imageUpload.ImageUploadAutoConfig;
import com.liangwj.tools2k.apiServer.security.LoginCheckAutoConfig;

/**
 * <pre>
 * 字典模块自动化配置
 * </pre>
 * 
 * @see DictProperties 配置文件
 * 
 * @author rock 2016年8月26日
 */
@Configuration
@EnableConfigurationProperties(DictProperties.class)
@ComponentScan(basePackageClasses = DictAutoConfig.class)
@Import(value = {
		ImageUploadAutoConfig.class, // 图片上传，附件管理要用
		LoginCheckAutoConfig.class, // loginCheck简易安全框架，包含debugMode和错误页面处理
})
public class DictAutoConfig {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DictAutoConfig.class);

	public DictAutoConfig() {
		log.debug("自动配置  DictAutoConfig");
	}
}
