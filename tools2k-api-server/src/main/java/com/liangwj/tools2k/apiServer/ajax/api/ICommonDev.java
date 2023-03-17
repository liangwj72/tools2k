package com.liangwj.tools2k.apiServer.ajax.api;

import com.liangwj.tools2k.annotation.api.AClass;
import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.annotation.api.AMethod;
import com.liangwj.tools2k.apiServer.ajax.api.beans.DevPageResponse;
import com.liangwj.tools2k.beans.exceptions.BaseApiException;

/**
 * <pre>
 * 查看ws api运行状态的相关方法
 * 
 * </pre>
 * 
 * @author rock
 */
@AClass
@AComment("框架-开发")
public interface ICommonDev {

	@AMethod(comment = "ajax的api列表")
	DevPageResponse ajaxApi() throws BaseApiException;
	
}
