package com.liangwj.tools2k.utils.other;

/**
 * <pre>
 * 用于StringUtil中的getStringFromMap方法
 * </pre>
 * 
 * @see StringUtilsEx#getStringFromMap(String, IGetStringFromMap)
 * 
 * @author rock 2015年8月4日
 */
public interface IGetStringFromMap {
	String get(String key);
}
