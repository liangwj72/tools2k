package com.liangwj.tools2k.db.druid;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 * 激活Druid连接池的监控系统
 *
 * @author rock
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(value = {
		MyDruidAutoConfig.class
})
public @interface EnableDruidStat {

}
