package com.liangwj.tools2k.apiServer.utils;

import org.springframework.util.StringUtils;

import com.liangwj.tools2k.annotation.api.AClass;
import com.liangwj.tools2k.annotation.api.AComment;

/**
 * <pre>
 * 放置搜索接口的结果
 * </pre>
 *
 * @author rock 2016年6月30日
 */
public class FindInferfaceResult {
	private final AClass aclass;
	private final Class<?> infClass;// 一个实现类是可以实现多个接口的

	private String infKey;

	public FindInferfaceResult(AClass aclass, Class<?> infClass) {
		super();
		this.aclass = aclass;
		this.infClass = infClass;
	}

	/**
	 * 获得接口类的备注
	 */
	public String getInfMemo() {
		AComment ann = this.infClass.getAnnotation(AComment.class);
		if (ann != null) {
			return ann.value();
		}
		return null;
	}

	/**
	 * 接口的class
	 *
	 * @return
	 */
	public Class<?> getInfClass() {
		return infClass;
	}

	/**
	 * 获得这个接口的key。
	 *
	 * <pre>
	 * 先看注解中是否有，如果没有就用类名，要去掉前面的I
	 * </pre>
	 *
	 * @return
	 */
	public String getInfKey() {
		if (this.infKey == null) {
			infKey = this.aclass.value();
			if (!StringUtils.hasText(infKey)) {
				// 如果注解中没有什么前缀，就用类名
				String classname = this.infClass.getSimpleName();
				if (classname.startsWith("I") && classname.length() > 1) {
					// 如果是I开头，就将i去掉
					infKey = classname.substring(1);
				} else {
					// 否则就直接用类名
					infKey = classname;
				}
				// 首字母小写
				infKey = StringUtils.uncapitalize(infKey);
			}

		}
		return infKey;
	}
}
