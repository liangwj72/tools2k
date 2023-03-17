package com.liangwj.tools2k.annotation.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * 接口中可被调用的方法。
 * 
 * 安全验证方面，需要在实现类中用 { ANeedCheckLogin} 注解。
 * ANeedCheckLogin 注意是在实现类中声明，而不是在接口中
 * </pre>
 * 
 * @author rock 2016年6月24日
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AMethod {

	/**
	 * 注释，可以为空
	 * 
	 * @return
	 */
	String comment() default "";

	/**
	 * 可以自定义客户端调用时的url，如果为空，默认是用方法名
	 * 
	 * @return
	 */
	String url() default "";

}
