package com.liangwj.tools2k.utils.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.util.Assert;

import com.liangwj.tools2k.annotation.form.AFormValidateMethod;
import com.liangwj.tools2k.beans.exceptions.FormValidateErrorInfoBean;
import com.liangwj.tools2k.beans.exceptions.ValidateFormException;
import com.liangwj.tools2k.utils.other.BaseThreadLocalHolder;
import com.liangwj.tools2k.utils.other.LogUtil;

/**
 * FORM绑定工具
 *
 * @author rock 2012-3-8
 */
public class BinderUtil {

	private static final org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
			.getLog(BinderUtil.class);

	public final static String DATE_FORMAT = "yyyy-MM-dd";

	public final static String DATE_FORMAT1 = "yyyy-MM-dd HH:mm";

	public final static String DATE_FORMAT2 = "yyyy-MM-dd HH:mm:ss";

	/** 默认时间查询周期，目前为60天 */
	public final static int DEFAUT_QUERY_DATE_RANGE = 60;

	/**
	 * 从request中获得form对象,不校验
	 *
	 * @param request
	 * @param formClazz
	 *            form的类
	 * @return
	 * @see AFormValidateMethod 校验的方法需要有 FormValidateMethod 这个声明
	 */
	public static <T> T bindForm(HttpServletRequest request, Class<T> formClazz) {
		return bindForm(request, formClazz, false);
	}

	/**
	 * 从request中获得form对象
	 *
	 * @param request
	 * @param formClazz
	 *            form的类
	 * @param validate
	 *            是否调用校验
	 * @return 表单对象
	 * @see AFormValidateMethod 校验的方法需要有 FormValidateMethod 这个声明
	 */
	public static <T> T bindForm(HttpServletRequest request, Class<T> formClazz, boolean validate) {
		try {
			T form = formClazz.getDeclaredConstructor().newInstance();

			final Map<String, String[]> map = new HashMap<>();
			final Enumeration<String> en = request.getParameterNames();
			while (en.hasMoreElements()) {
				final String key = en.nextElement();
				final String[] value = request.getParameterValues(key);
				map.put(key, value);
			}

			BinderEditorSupport.updateObj(request, map, form, validate);

			return form;
		} catch (final Exception e) {
			LogUtil.traceError(log, e);
			return null;
		}
	}

	/**
	 * 从session中获取表单
	 *
	 * @param request
	 *            HttpServletRequest
	 * @param formClazz
	 *            表单类型
	 * @param sessionName
	 *            session的名字
	 * @return
	 */
	public static <T> T getFormFromSession(HttpServletRequest request, Class<T> formClazz, String sessionName) {

		Assert.hasText(sessionName, "sessionName不能为空");

		@SuppressWarnings("unchecked")
		T form = (T) request.getSession().getAttribute(sessionName);

		if (form == null) {
			try {
				form = formClazz.getDeclaredConstructor().newInstance();
			} catch (final Throwable e) {
				throw new RuntimeException("创建表单实例时出错了 表单:" + formClazz.getName(), e);
			}
		}
		return form;

	}

	/** 表单校验器的存储器 */
	private static BaseThreadLocalHolder<Validator> validatorHolder = new BaseThreadLocalHolder<Validator>() {
		@Override
		protected Validator createObj() {
			final Validator validator = javax.validation.Validation.buildDefaultValidatorFactory().getValidator();
			return validator;
		}
	};

	/**
	 * 校验form，并且返回一个可通过json返回到页面的list
	 *
	 * @param form
	 *            要验证的表单
	 * @throws ValidateFormException
	 *             验证出错时的信息
	 */
	public static void validateForm(Object form) throws ValidateFormException {
		if (form == null) {
			return;
		}

		// 验证某个对象,，其实也可以只验证其中的某一个属性的
		final Set<ConstraintViolation<Object>> constraintViolations = validatorHolder.getObj().validate(form);

		if (!constraintViolations.isEmpty()) {

			final List<FormValidateErrorInfoBean> list = new LinkedList<>();
			// 如果验证通过，就没有error
			for (final ConstraintViolation<Object> error : constraintViolations) {
				final String fieldName = error.getPropertyPath().toString();
				final String errorMsg = error.getMessage();

				final FormValidateErrorInfoBean bean = new FormValidateErrorInfoBean(fieldName, errorMsg);

				list.add(bean);
			}

			final ValidateFormException ex = new ValidateFormException(form, list);

			throw ex;
		}
	}
}
