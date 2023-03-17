package com.liangwj.tools2k.apiServer.dict.api.beans;

import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.annotation.form.AForm;

/**
 * <pre>
 * 附件编辑表单
 * </pre>
 * 
 * @author rock 2016年11月16日
 */
@AForm
public class DictAttachmentEditForm {

	@NotEmpty(message = "Key不能为空")
	private String key;

	private MultipartFile file;

	/** 备注 */
	private String memo;

	public String getKey() {
		return key;
	}

	public String getMemo() {
		return memo;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	@AComment(value = "键值")
	public void setKey(String key) {
		this.key = key;
	}

	@AComment(value = "备注")
	public void setMemo(String memo) {
		this.memo = memo;
	}

}
