package com.liangwj.tools2k.apiServer.dict.api.beans;

import org.springframework.util.StringUtils;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.annotation.form.AForm;
import com.liangwj.tools2k.apiServer.dict.DictXml.DictAttachmentRow;
import com.liangwj.tools2k.apiServer.dict.DictXml.DictXmlRow;
import com.liangwj.tools2k.beans.form.PageForm;

/**
 * <pre>
 * 字典查询表单
 * </pre>
 * 
 * @author rock 2015年8月4日
 */
@AForm
public class KeySearchPageForm extends PageForm {

	private String key;

	public String getKey() {
		return key;
	}

	@AComment(value = "关键词")
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * 判断这一行中是否包含了查询的关键词
	 */
	public boolean isMatch(DictXmlRow row) {
		if (!StringUtils.hasText(key)) {
			// 搜索关键词为空的时候，返回真
			return true;
		} else {
			// 非空的时候
			if (StringUtils.hasText(row.getKey())) {
				return row.getKey().contains(key);
			}
			return false;
		}
	}

	/**
	 * 判断这一行中是否包含了查询的关键词
	 */
	public boolean isMatch(DictAttachmentRow row) {
		if (!StringUtils.hasText(key)) {
			// 搜索关键词为空的时候，返回真
			return true;
		} else {
			// 非空的时候
			if (StringUtils.hasText(row.getKey())) {
				return row.getKey().contains(key);
			}
			return false;
		}
	}

}
