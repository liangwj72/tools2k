package com.liangwj.tools2k.utils.other;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.JMException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.liangwj.tools2k.beans.system.ClassLoadingInfo;
import com.liangwj.tools2k.beans.system.OsInfo;
import com.liangwj.tools2k.beans.system.ThreadingInfo;
import com.liangwj.tools2k.beans.system.VmFullInfo;
import com.liangwj.tools2k.beans.system.VmInfo;
import com.liangwj.tools2k.utils.classUtils.MethodUtil;
import com.liangwj.tools2k.utils.classUtils.MethodUtil.MethodInfoOfSetter;

/**
 * <pre>
 * 为mbean提供的工具集合
 * </pre>
 * 
 * @author rock 2016年10月12日
 */
public class MBeanUtils {

	/**
	 * <pre>
	 * 在执行  objectToMap 时，是否忽略该方法
	 * </pre>
	 * 
	 * @author rock 2016年10月12日
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	@Documented
	public static @interface IgnoreWhenObjectToMap {
		/** 备注 */
		String value() default "";
	}

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MBeanUtils.class);

	/** 获得虚拟机中关于类加载的信息 */
	public static ClassLoadingInfo getClassLoadingInfo() {
		ClassLoadingInfo bean = new ClassLoadingInfo();
		updateMBeanAttrInfoToBean("java.lang:type=ClassLoading", bean);
		return bean;
	}

	/** 获得虚拟机中关于线程的信息 */
	public static ThreadingInfo getThreadingInfo() {
		ThreadingInfo bean = new ThreadingInfo();
		updateMBeanAttrInfoToBean("java.lang:type=Threading", bean);
		return bean;
	}

	/** 虚拟机的信息 */
	public static VmInfo getVmInfo() {
		VmInfo bean = new VmInfo();
		updateMBeanAttrInfoToBean("java.lang:type=Runtime", bean);
		return bean;
	}

	/** 虚拟机的信息，包含classpath等完全版的信息 */
	public static VmFullInfo getVmFullInfo() {
		VmFullInfo bean = new VmFullInfo();
		updateMBeanAttrInfoToBean("java.lang:type=Runtime", bean);
		return bean;
	}

	public static OsInfo getOsInfo() {
		OsInfo bean = new OsInfo();
		updateMBeanAttrInfoToBean("java.lang:type=OperatingSystem", bean);
		return bean;
	}

	/**
	 * 将对象的内容输出到map
	 * 
	 * @param obj
	 *            要输出的对象
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> objectToMap(Object obj) throws Exception {

		Assert.notNull(obj, "要转换的对象不能为空");

		Map<String, Object> map = new HashMap<>();

		BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor property : propertyDescriptors) {
			String key = property.getName();
			if ("class".equals(key)) {
				// 过滤getClass()
				continue;
			}

			Method getter = property.getReadMethod();
			if (getter != null) {
				Object value;

				IgnoreWhenObjectToMap anno = getter.getAnnotation(IgnoreWhenObjectToMap.class);
				if (anno != null) {
					if (StringUtils.hasText(anno.value())) {
						// 如果注解上有备注就用备注
						value = anno.value();
					} else {
						value = String.format("%s属性设置了忽略", key);
					}
				} else {
					value = getter.invoke(obj);
				}
				map.put(key, value);
			}
		}

		return map;
	}

	/**
	 * 将MBean中属性的值设置到 bean中
	 * 
	 * @param objectNameStr
	 *            mbean的名字
	 * @param bean
	 *            要设置的bean
	 */
	private static void updateMBeanAttrInfoToBean(String objectNameStr, Object bean) {
		Assert.notNull(objectNameStr, "MBean objectName不能为空");
		Assert.notNull(bean, "结果bean不能为空");

		// 将目标对象的所有setter都放到一个map中
		Map<String, MethodInfoOfSetter> setterMap = new HashMap<>();
		List<MethodInfoOfSetter> setters = MethodUtil.findSetter(bean.getClass());
		for (MethodInfoOfSetter setter : setters) {
			setterMap.put(setter.getPropName(), setter);
		}

		MBeanServer server = ManagementFactory.getPlatformMBeanServer();

		try {
			// 生成mbean名字
			ObjectName objectName = new ObjectName(objectNameStr);

			if (!objectName.isPattern() && server.isRegistered(objectName)) {
				// 必须有注册这个mbean
				MBeanInfo info = server.getMBeanInfo(objectName);
				// 遍历所有的属性
				for (MBeanAttributeInfo attr : info.getAttributes()) {
					// 获取收字母小写的名字
					String key = StringUtils.uncapitalize(attr.getName());

					// System.out.println(String.format("private %s %s;",
					// attr.getType(), attr.getName()));

					MethodInfoOfSetter setter = setterMap.get(key);
					if (setter != null) {
						// 如果能找到setter，就获取bean的值
						Object value = server.getAttribute(objectName, attr.getName());

						// 并加值设置进去
						try {
							setter.getOriginMethod().invoke(bean, value);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							log.warn("调用 {} 的 {} 方法时，发生了错误，value={}",
									bean.getClass(), setter.getOriginMethod().getName(), value);
						}
					}
				}

				// 迭代属性，并调用处理器
			}
		} catch (JMException e) {
			// 如果出错就啥都不做
			LogUtil.traceError(log, e, "获取MBean信息时，出现了错误, objectName=" + objectNameStr);
		}
	}

	/**
	 * 将MBean中属性的名字打印出来，用于自动生成一段代码，写bean类代码是方便
	 * 
	 * @param objectNameStr
	 *            mbean的名字
	 * @throws JMException
	 */
	protected static void printMBeanAttr(String objectNameStr) throws JMException {
		Assert.hasText(objectNameStr, "MBean objectName不能为空");

		MBeanServer server = ManagementFactory.getPlatformMBeanServer();

		// 生成mbean名字
		ObjectName objectName = new ObjectName(objectNameStr);

		if (!objectName.isPattern() && server.isRegistered(objectName)) {
			// 必须有注册这个mbean
			MBeanInfo info = server.getMBeanInfo(objectName);
			// 遍历所有的属性
			for (MBeanAttributeInfo attr : info.getAttributes()) {
				// 输出到控制台，输出的容易用于粘贴到代码中
				System.out.println("@AComment(\"\")");
				System.out.println(String.format("private %s %s;",
						attr.getType(),
						StringUtils.uncapitalize(attr.getName())));
			}
		}
	}

	private MBeanUtils() {
		// 工具类不允许生成实例
	}
}
