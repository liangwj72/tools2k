package com.liangwj.tools2k.apiServer.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.liangwj.tools2k.apiServer.websocket.WsApiServerAutoConfig;

/**
 * api server服务 websocket接口
 * 
 * @author 梁韦江
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableWebPorjectSet
@Import(value = {
		WsApiServerAutoConfig.class,
})
public @interface EnableWsApiServer {

}
