package com.liangwj.tools2k.apiServer.dict.api;

import com.liangwj.tools2k.annotation.api.AClass;
import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.annotation.api.AMethod;
import com.liangwj.tools2k.apiServer.beans.CommonSuccessResponse;
import com.liangwj.tools2k.apiServer.dict.api.beans.DictImportForm;
import com.liangwj.tools2k.apiServer.dict.api.beans.DictKeyForm;
import com.liangwj.tools2k.apiServer.dict.api.beans.DictKeySearchResponse;
import com.liangwj.tools2k.apiServer.dict.api.beans.DictRowEditForm;
import com.liangwj.tools2k.apiServer.dict.api.beans.KeySearchPageForm;
import com.liangwj.tools2k.beans.exceptions.BaseApiException;

/**
 * <pre>
 * 字典管理接口
 * </pre>
 * 
 * @author rock 2016年11月16日
 */
@AClass("dictManager")
@AComment(value = "字典-词条管理")
public interface IDictManager {

	@AMethod(comment = "根据关键字搜索")
	DictKeySearchResponse search(KeySearchPageForm form);

	@AMethod(comment = "新增或者保存键值")
	DictKeySearchResponse save(DictRowEditForm form) throws BaseApiException;

	@AMethod(comment = "导入xml")
	CommonSuccessResponse importXml(DictImportForm form) throws BaseApiException;

	@AMethod(comment = "删除")
	DictKeySearchResponse delete(DictKeyForm form) throws BaseApiException;

}
