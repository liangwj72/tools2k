package com.liangwj.tools2k.apiServer.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.liangwj.tools2k.apiServer.web.WebProjectAutoConfig;

/**
 * 自动配置 web项目的常用组件
 *
 * @author rock 2016年11月30日
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(value = {
		WebProjectAutoConfig.class, // JMX 页面
})
public @interface EnableWebPorjectSet {

}
