package com.liangwj.tools2k.apiServer.dict.api;

import java.io.IOException;

import com.liangwj.tools2k.annotation.api.AClass;
import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.annotation.api.AMethod;
import com.liangwj.tools2k.apiServer.dict.api.beans.DictAttachmentEditForm;
import com.liangwj.tools2k.apiServer.dict.api.beans.DictAttachmentSearchResponse;
import com.liangwj.tools2k.apiServer.dict.api.beans.DictKeyForm;
import com.liangwj.tools2k.beans.exceptions.BaseApiException;

/**
 * <pre>
 * 字典管理接口
 * </pre>
 * 
 * @author rock 2016年11月16日
 */
@AClass("dictAttachments")
@AComment(value = "字典-附件文件管理")
public interface IDictAttechmentManager {

	@AMethod(comment = "获取所有附件")
	DictAttachmentSearchResponse list();

	@AMethod(comment = "新增或者保存键值")
	DictAttachmentSearchResponse save(DictAttachmentEditForm form) throws BaseApiException, IOException;

	@AMethod(comment = "删除")
	DictAttachmentSearchResponse delete(DictKeyForm form) throws BaseApiException, IOException;

}
