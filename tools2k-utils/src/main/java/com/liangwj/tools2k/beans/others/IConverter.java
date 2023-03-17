package com.liangwj.tools2k.beans.others;

/**
 * <pre>
 * 转换器接口
 * </pre>
 * 
 * @author rock
 *  2016年8月22日
 */
public interface IConverter<SRC, TARGET> {

	/**
	 * 将一个对象换成为另外一个对象
	 * 
	 * @param src
	 *            原始数据
	 * @return 目标数据
	 */
	TARGET convert(SRC src);
}
