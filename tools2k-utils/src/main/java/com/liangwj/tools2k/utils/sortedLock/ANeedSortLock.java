package com.liangwj.tools2k.utils.sortedLock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 有此注解的方法，表示要用顺序加锁，通常用于service中的方法
 * 
 * @author rock
 */
@Target({ ElementType.METHOD })
public @interface ANeedSortLock {

}
