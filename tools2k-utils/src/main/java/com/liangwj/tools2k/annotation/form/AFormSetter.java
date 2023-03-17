package com.liangwj.tools2k.annotation.form;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * 用于MethodUtil查找setter时
 * </pre>
 * 
 * @author rock
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Documented
public @interface AFormSetter {

	/** 定义这个setter的名字 */
	String propName();
}
