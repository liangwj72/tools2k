package com.liangwj.tools2k.db.druid.api;

import com.liangwj.tools2k.annotation.api.AClass;
import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.annotation.api.AMethod;
import com.liangwj.tools2k.beans.exceptions.BaseApiException;
import com.liangwj.tools2k.beans.form.IdForm;
import com.liangwj.tools2k.db.druid.beans.DruidInfoResponse;
import com.liangwj.tools2k.db.druid.beans.DruidListResponse;

/**
 * <pre>
 * 字典管理用户接口
 * </pre>
 *
 * @author rock 2016年11月16日
 */
@AClass("commonDurid")
@AComment(value = "框架-监控")
public interface ICommonDruid {

	@AMethod(comment = "获取Basic")
	DruidInfoResponse getBasic() throws BaseApiException;

	@AMethod(comment = "获取Datasources")
	DruidListResponse getDatasources() throws BaseApiException;

	@AMethod(comment = "获取根据datasource的Id获取该数据源的sql请求统计")
	DruidListResponse getSqlByDatasourceId(IdForm id) throws BaseApiException;

	@AMethod(comment = "获取每个请求中发生的sql统计")
	DruidListResponse getWebUris() throws BaseApiException;

	@AMethod(comment = "获取应用总览")
	DruidListResponse getWebApp() throws BaseApiException;

	@AMethod(comment = "获取session")
	DruidListResponse getSessions() throws BaseApiException;

	@AMethod(comment = "重置所有")
	void resetAll() throws BaseApiException;


}
