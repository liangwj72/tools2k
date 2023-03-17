package com.liangwj.tools2k.utils.sortedLock;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 * 排序加锁服务，用于写避免死锁的服务
 * 
 * @see INeedSortLockObj 有锁的对象须有实现该接口
 * @see ANeedSortLock 须排序加锁的方法上需要这个注解
 * 
 * @author rock 2016年8月11日
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(SortedLockAutoConfig.class)
public @interface EnableSortedLock {

}
