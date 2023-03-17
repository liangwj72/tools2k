package com.liangwj.tools2k.utils.other;

import java.io.File;

import com.liangwj.tools2k.beans.system.DiskInfo;
import com.liangwj.tools2k.beans.system.OsMemoryInfoBean;

/**
 * <pre>
 * 操作系统系统工具
 * </pre>
 * 
 * @author rock 2016年12月17日
 */
public class OsUtil {

	/**
	 * 获得内存使用情况
	 */
	public static OsMemoryInfoBean getMemoryInfo() {
		OsMemoryInfoBean bean = new OsMemoryInfoBean();
		bean.setFreeMemory(getFreeMemory());
		bean.setMaxMemory(getMaxMemory());
		bean.setTotalMemory(getTotalMemory());
		bean.setUsedMemory(getUsedMemory());
		return bean;
	}

	/**
	 * 获取硬盘信息
	 */
	public static DiskInfo getDiskInfo() {
		File file = new File(".");

		DiskInfo bean = new DiskInfo();
		bean.setFreeSpace(file.getFreeSpace());
		bean.setTotalSpace(file.getTotalSpace());
		bean.setUsableSpace(file.getUsableSpace());

		return bean;
	}

	/**
	 * 获取已经分配的内存总数
	 */
	public static long getTotalMemory() {
		return Runtime.getRuntime().totalMemory();
	}

	/**
	 * 获取可分配的内存总数
	 */
	public static long getFreeMemory() {
		return Runtime.getRuntime().freeMemory();
	}

	/**
	 * 获取可分配的最大内存数
	 */
	public static long getMaxMemory() {
		return Runtime.getRuntime().maxMemory();
	}

	/**
	 * 获取已经使用的内存数
	 */
	public static long getUsedMemory() {
		return getTotalMemory() - getFreeMemory();
	}

}
