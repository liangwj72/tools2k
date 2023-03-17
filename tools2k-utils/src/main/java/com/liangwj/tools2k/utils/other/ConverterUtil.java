package com.liangwj.tools2k.utils.other;

import java.util.ArrayList;
import java.util.List;

import com.liangwj.tools2k.beans.others.IConverter;

/**
 * <pre>
 * 转换器工具，配合IConverter
 * </pre>
 * 
 * @see IConverter
 * 
 * @author rock 2016年8月31日
 */
public class ConverterUtil {

	/**
	 * 将list转换成为另外一个类型
	 * 
	 * @param list
	 * @param converter
	 * @return
	 */
	public static <SRC, TARGET> List<TARGET> convertList(List<SRC> list, IConverter<SRC, TARGET> converter) {
		if (list == null) {
			return null;
		}
		List<TARGET> res = new ArrayList<>(list.size());
		for (SRC po : list) {
			res.add(converter.convert(po));
		}
		return res;
	}
}
