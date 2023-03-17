package com.liangwj.tools2k.utils.other;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.util.Assert;

import com.liangwj.tools2k.utils.classUtils.ClassDescriber;

/**
 * Spring 用到的各类工具
 *
 * @author rock
 *
 */
public class SpringUtil {

	/** 默认的pid文件名 */
	public static final String DEFAULT_PID_FILE = "run.pid";

	/** 用极简参数启动spring 应用 */
	public static void startServer(Class<?> applicationClass) {
		startServer(applicationClass, DEFAULT_PID_FILE, new String[0]);
	}
	/** 用默认配置启动spring 应用 */
	public static void startServer(Class<?> applicationClass, String[] args) {
		startServer(applicationClass, DEFAULT_PID_FILE, args);
	}

	/** 全部参数的，启动spring应用 */
	public static void startServer(Class<?> applicationClass, String pidName, String[] args) {

		Assert.notNull(applicationClass, "启动类不能未空");
		Assert.notNull(pidName, "pid文件名不能未空");

		String pkg = applicationClass.getPackage().getName();

		// 过滤器也需要加入启动程序的包名
		ClassDescriber.addPackageToDefaultFilter(pkg); // 用于通过反射方式获取类的说明信息
		LogUtil.addPackage(pkg); // 日志中要包含这个包

		SpringApplication sa = new SpringApplication(applicationClass);
		sa.addListeners(new ApplicationPidFileWriter(pidName));
		sa.run(args);
	}

}
