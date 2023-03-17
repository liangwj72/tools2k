package com.liangwj.tools2k.beans.others;

/**
 * <pre>
 * 过滤器
 * </pre>
 * 
 * @author rock
 *  2016年8月25日
 */
public interface IFilter<T> {

	boolean isMatch(T obj);

}
