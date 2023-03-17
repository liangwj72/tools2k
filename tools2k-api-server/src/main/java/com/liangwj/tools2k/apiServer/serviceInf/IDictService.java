package com.liangwj.tools2k.apiServer.serviceInf;

import java.util.Map;

/**
 * <pre>
 * 字典服务的接口
 * </pre>
 * 
 * @author rock
 * 
 */
public interface IDictService {

	/**
	 * 将所有的内容以key / value的格式输出
	 */
	public Map<String, String> toJsonMap();

	/**
	 * 根据key获取字典定义，
	 * 
	 * @param key
	 *            键值
	 * @param defaultValue
	 *            默认值
	 * @return 原始文本
	 */
	public String getRawText(String key, String defaultValue);

	/**
	 * 获取字典配置的系统名字
	 */
	public String getSystemName();

}
