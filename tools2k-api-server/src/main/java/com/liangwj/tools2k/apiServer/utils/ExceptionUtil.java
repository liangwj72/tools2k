package com.liangwj.tools2k.apiServer.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartException;

import com.liangwj.tools2k.annotation.api.ADataInApiException;
import com.liangwj.tools2k.apiServer.beans.CommonErrorResponse;
import com.liangwj.tools2k.apiServer.beans.FormVaildateErrorResponse;
import com.liangwj.tools2k.beans.exceptions.BaseApiException;
import com.liangwj.tools2k.beans.exceptions.MissFieldException;
import com.liangwj.tools2k.beans.exceptions.SystemErrorException;
import com.liangwj.tools2k.beans.exceptions.ValidateFormException;
import com.liangwj.tools2k.utils.classUtils.MethodUtil;
import com.liangwj.tools2k.utils.classUtils.MethodUtil.MethodInfoOfGetter;
import com.liangwj.tools2k.utils.other.EmailUtil;
import com.liangwj.tools2k.utils.other.LogUtil;

import jakarta.servlet.http.HttpServletResponse;

/**
 * <pre>
 * 用于抛错的工具
 * </pre>
 *
 * @author rock 2016年9月2日
 */
public class ExceptionUtil {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ExceptionUtil.class);

	public static void notNull(Object obj, String message) throws MissFieldException {
		if (obj == null) {
			throw new MissFieldException(message);
		}
	}

	public static void hasText(String str, String message) throws MissFieldException {
		if (!StringUtils.hasText(str)) {
			throw new MissFieldException(message);
		}
	}

	public static void isEmail(String str, String message) throws MissFieldException {
		if (!StringUtils.hasText(str) || !EmailUtil.isEmail(str)) {
			throw new MissFieldException(message);
		}
	}

	public static void notZero(int value, String message) throws MissFieldException {
		if (value <= 0) {
			throw new MissFieldException(message);
		}
	}

	/**
	 * 根据错误信息，返回不同类型的 json返回
	 */
	public static CommonErrorResponse getErrorResponse(HttpServletResponse response, Throwable exParam, boolean debugMode) {
		CommonErrorResponse res;

		int statusCode = 400;

		if (exParam == null) {
			res = new CommonErrorResponse("未知错误");
		} else {
			Throwable e = exParam;
			if (e instanceof InvocationTargetException) {
				// 如果类型是 InvocationTargetException， 里面的类型才是真正的错误
				e = ((InvocationTargetException) exParam).getTargetException();
			}

			if (e instanceof BaseApiException) {

				final BaseApiException e1 = (BaseApiException) e;

				statusCode = e1.getHttpStatusCode();

				log.debug("调用api时发生逻辑错误: {} ", e1.getErrorMsg());

				// 如果是逻辑错误，就将逻辑错误变成response
				res = new CommonErrorResponse(e1);
				// 同时设置额外的数据进去
				res.setExData(getExDataFromApiException(e1));

			} else if (e instanceof ValidateFormException) {
				log.debug("调用api, 表单验证不通过 ");
				res = new FormVaildateErrorResponse((ValidateFormException) e);
				statusCode=HttpStatus.PRECONDITION_FAILED.value(); //412
			} else {
				// 其他错误时，返回系统错误
				LogUtil.traceError(log, e, "调用api时发生其他错误 ");
				res = getSystemErrorResponse(e, debugMode);
			}
		}

		response.setStatus(statusCode);

		return res;
	}

	/**
	 * 获取ApiException上的额外数据
	 */
	public static Map<String, Object> getExDataFromApiException(BaseApiException ex) {
		if (ex == null) {
			return null;
		}

		final Map<String, Object> map = new HashMap<>();
		final Class<?> clazz = ex.getClass();

		final List<MethodInfoOfGetter> getterList = MethodUtil.findGetter(clazz);
		for (final MethodInfoOfGetter mi : getterList) {
			if (mi.isOpenType() && mi.getAnnotation(ADataInApiException.class, false) != null) {
				// 如果是 系统的类型，并且属性上ADataInApiException有注解
				Object value = null;
				try {
					value = mi.getOriginMethod().invoke(ex);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				}
				if (value != null) {
					map.put(mi.getPropName(), value);
				}
			}
		}

		return map;
	}

	private static String getSize(long size) {
		if (size >= 1024 * 1024) {
			final long t1 = 10 * size / 1024 / 1024;
			final float t2 = t1 / 10f;
			return String.format("%.1fM", t2);
		} else {
			final long t1 = 10 * size / 1024;
			final float t2 = t1 / 10f;
			return String.format("%.1fK", t2);
		}
	}

	/**
	 * 系统运行时错误
	 *
	 * @param ex
	 *            Throwable
	 * @param debugMode
	 *
	 * @return CommonErrorResponse
	 */
	private static CommonErrorResponse getSystemErrorResponse(Throwable ex, boolean debugMode) {

		CommonErrorResponse res;
		if (ex instanceof MultipartException) {
			String msg = "上传文件时发生了错误";

			final Throwable root = ((MultipartException) ex).getRootCause();
			if (root != null) {
				if (root instanceof FileSizeLimitExceededException) {
					final FileSizeLimitExceededException e = (FileSizeLimitExceededException) root;

					msg = String.format("上传文件不能超过 %s", getSize(e.getPermittedSize()));
				}
			}

			res = new CommonErrorResponse(ex, msg);
		} else {

			final SystemErrorException error = new SystemErrorException(ex);
			res = new CommonErrorResponse(error);
		}
		if (debugMode) {
			// 如果是开发模式，将调查错误过程回馈给客户端
			final String str = LogUtil.getTraceString(null, ex);
			// 过滤掉中间 \t \r \n 之类的字符
			res.setMessage(com.liangwj.tools2k.utils.other.StringUtilsEx.trimMiddleWhitespace(str));
		}
		return res;

	}
}
