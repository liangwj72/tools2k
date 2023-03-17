package com.liangwj.tools2k.apiServer.ajax.api;

import com.liangwj.tools2k.annotation.api.AClass;
import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.annotation.api.AMethod;
import com.liangwj.tools2k.apiServer.beans.CommonSuccessResponse;
import com.liangwj.tools2k.apiServer.beans.manager.OsInfoResponse;
import com.liangwj.tools2k.apiServer.beans.manager.RuntimeHistoryResponse;
import com.liangwj.tools2k.apiServer.beans.manager.UrlStatResponse;
import com.liangwj.tools2k.apiServer.beans.manager.WsConnectInfoResponse;
import com.liangwj.tools2k.beans.exceptions.BaseApiException;
import com.liangwj.tools2k.beans.form.IdForm;
import com.liangwj.tools2k.beans.form.PageForm;

/**
 * <pre>
 * 查看ws api运行状态的相关方法
 *
 * </pre>
 *
 * @author rock
 */
@AClass
@AComment("框架-运行状态")
public interface ICommonRuntime {

	@AMethod(comment = "ws连接列表")
	WsConnectInfoResponse wsConnectList(PageForm from) throws BaseApiException;

	@AMethod(comment = "重试ws计数器")
	CommonSuccessResponse wsResetCounter() throws BaseApiException;

	@AMethod(comment = "运行图表")
	RuntimeHistoryResponse runtimeHistory() throws BaseApiException;

	@AMethod(comment = "操作系统和虚拟机信息")
	OsInfoResponse osInfo() throws BaseApiException;

	@AMethod(comment = "根据ID获取URL统计")
	UrlStatResponse getUrlStatById(IdForm form) throws BaseApiException;
}
