package com.liangwj.tools2k.utils.other;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;

import com.liangwj.tools2k.beans.others.IFilter;

/**
 * 日志帮助类
 *
 * @author rock
 *
 */
public class LogUtil {

	private final static LogUtil instance = new LogUtil();

	/** 包名白名单包 */
	private final Set<String> packageWhiteList = new HashSet<>();

	private LogUtil() {
		this.packageWhiteList.add("com.cfido");
	}

	/**
	 * 只显示 部分错误信息的 Filter
	 */
	private final IFilter<StackTraceElement> filter = (obj) -> {

		if (obj != null) {
			String className = obj.getClassName();
			for (String name : this.packageWhiteList) {
				if (className.startsWith(name)) {
					return true;
				}
			}
		}
		// return true;
		return false;
	};

	/** 增加包名白名单 */
	public static void addPackage(String packageName) {
		if (StringUtils.hasText(packageName)) {
			instance.packageWhiteList.add(packageName);
		}
	}

	/**
	 * 打印出错详细过程，默认情况是只显示 和com.linzi相关的trace
	 */
	public static void traceError(Log log, Throwable e) {
		log.error(getTraceString(null, e));
	}

	/**
	 * 打印出错详细过程，默认情况是只显示 和com.linzi相关的trace
	 */
	public static void traceError(Log log, Throwable e, String errorMsg) {
		log.error(getTraceString(errorMsg, e));
	}

	/**
	 * 获得到当前执行处的调用过程作为字符串返回，msg是要放到字符串前面的内容，方便直接用log
	 *
	 * @param msg
	 * @return
	 */
	public static String getStackTrace(String msg) {
		StringWriter w = new StringWriter();
		PrintWriter out = new PrintWriter(w);

		if (StringUtils.hasText(msg)) {
			out.println(msg);
		}
		StackTraceElement[] traces = Thread.currentThread().getStackTrace();
		int len = traces.length - 3;
		if (len > 0) {
			// 前三行没用的
			StackTraceElement[] t3 = new StackTraceElement[len];
			System.arraycopy(traces, 3, t3, 0, len);
			instance.printStackTraceElements(out, t3);
		}

		return w.toString();
	}

	public static void traceWarn(Log log, String errorMsg) {
		log.warn(getStackTrace(errorMsg));
	}

	public static void traceWarn(Logger log, String errorMsg) {
		log.warn(getStackTrace(errorMsg));
	}

	/**
	 * 获取调用过程，默认情况是只显示 和com.linzi相关的trace
	 */
	public static String getTraceString(String errorMsg, Throwable e) {
		StringWriter w = new StringWriter();
		PrintWriter out = new PrintWriter(w);

		if (StringUtils.hasText(errorMsg)) {
			// 如果有errorMsg，就输入
			out.println(errorMsg);
		}

		// 构建防止重复输出的set
		Set<Throwable> dejaVu = Collections.newSetFromMap(new IdentityHashMap<Throwable, Boolean>());

		// 递归打印所有的错误信息
		instance.printThrowable(out, e, dejaVu);

		return w.toString();
	}

	/**
	 * 打印 Throwable
	 *
	 * @param out
	 * @param e
	 * @param set
	 */
	private void printThrowable(PrintWriter out, Throwable e, Set<Throwable> set) {
		if (set.contains(e)) {
			return;
		}
		set.add(e);

		out.println(e.toString());
		this.printStackTraceElements(out, e.getStackTrace());

		Throwable cause = e.getCause();
		if (cause != null) {
			out.print("Cause by:");
			printThrowable(out, cause, set);
		}
	}

	/**
	 * 输出一行错误
	 *
	 * @param out
	 * @param traces
	 */
	private void printStackTraceElements(PrintWriter out, StackTraceElement[] traces) {

		if (traces == null) {
			return;
		}

		boolean skip = false;
		for (StackTraceElement trace : traces) {
			if (filter.isMatch(trace)) {
				out.println("\tat " + trace);
				skip = false;
			} else {
				if (!skip) {
					out.println("\t...");
				}
				skip = true;
			}
		}
	}

	/**
	 * 不会抛错的String.format
	 *
	 * @param format
	 * @param args
	 * @return
	 */
	public static String format(String format, Object... args) {
		try {
			return String.format(format, args);
		} catch (Exception e) {
			StringBuffer sb = new StringBuffer();
			sb.append(format).append(' ').append(e.getMessage()).append('\t');
			for (int i = 0; i < args.length; i++) {
				sb.append(args[i]).append(" ");
			}
			return sb.toString();
		}
	}

	/**
	 * 打印LOG用
	 *
	 * @param hsql
	 * @param params
	 */
	public static String printSql(String hsql, Object[] params) {
		return SqlUtils.printSql(hsql, params);
	}

	/**
	 * 打印出错详细过程，默认情况是只显示 和com.linzi相关的trace
	 */
	public static void traceError(Logger log, Throwable e) {
		log.error(getTraceString(null, e));
	}

	/**
	 * 打印出错详细过程，默认情况是只显示 和com.linzi相关的trace
	 */
	public static void traceError(Logger log, Throwable e, String errorMsg) {
		log.error(getTraceString(errorMsg, e));
	}
}
