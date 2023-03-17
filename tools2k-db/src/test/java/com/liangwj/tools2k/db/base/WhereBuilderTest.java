package com.liangwj.tools2k.db.base;


import org.junit.jupiter.api.Test;

import com.liangwj.tools2k.annotation.form.ABuildWhereFieldName;
import com.liangwj.tools2k.beans.form.PageForm;

/**
 * <pre>
 *
 * </pre>
 *
 * @author rock 2015年7月19日
 */
public class WhereBuilderTest {

	/**
	 * <pre>
	 * 企业列表查询条件Form
	 * </pre>
	 *
	 * @author 黄云 2015-6-17
	 */
	public class InsSearchForm extends PageForm {

		private String test;

		@ABuildWhereFieldName(name = "testField")
		public String getTest() {
			return test;
		}

		public void setTest(String test) {
			this.test = test;
		}
	}

	@Test
	public void test() {
		InsSearchForm form = new InsSearchForm();
		form.setTest("test");
		WhereBuilder builder = WhereBuilder.create(form);

		System.out.println(builder.toString());
	}

}
