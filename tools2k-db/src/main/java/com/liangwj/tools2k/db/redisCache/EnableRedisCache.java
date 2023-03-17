package com.liangwj.tools2k.db.redisCache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 自动设置 使用Redis的CacheManager
 * 
 * <pre>
 * 主要就是设置redis的序列化工具
 * 
 * Cache操作中有异步操作，所以需要 @EnableAsync
 * </pre>
 * 
 * @author rock
 *  2016年8月11日
 * 
 * @see RedisCacheProperties 可配置参数
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
// @EnableCaching
@EnableAsync
@Import(RedisCacheConfig.class)
public @interface EnableRedisCache {

}
