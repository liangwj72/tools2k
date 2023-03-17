package com.liangwj.tools2k.apiServer.dict.api.beans;

import org.springframework.web.multipart.MultipartFile;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.annotation.form.AForm;

/**
 * <pre>
 * 用于导入xml的表单
 * </pre>
 * 
 * @author rock 2016年11月17日
 */
@AForm
public class DictImportForm {

	private MultipartFile file;

	private boolean cleanOld;

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public boolean isCleanOld() {
		return cleanOld;
	}

	@AComment(value = "导入时，是否清空原有数据")
	public void setCleanOld(boolean cleanOld) {
		this.cleanOld = cleanOld;
	}

}
