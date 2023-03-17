package com.liangwj.tools2k.apiServer.dict.api;

import com.liangwj.tools2k.annotation.api.AClass;
import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.annotation.api.AMethod;
import com.liangwj.tools2k.apiServer.dict.api.beans.DictPublicResponse;

/**
 * <pre>
 * 字典管理用户接口
 * </pre>
 * 
 * @author rock 2016年11月16日
 */
@AClass("dictPublic")
@AComment(value = "字典-公开")
public interface IDictPublic {

	@AMethod(comment = "获得字典定义的所有内容")
	DictPublicResponse getDict();
}
