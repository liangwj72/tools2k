package com.liangwj.tools2k.db.druid.api;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Service;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.druid.stat.DruidStatManagerFacade;
import com.alibaba.druid.support.http.stat.WebAppStatManager;
import com.liangwj.tools2k.annotation.api.AApiServerImpl;
import com.liangwj.tools2k.apiServer.loginCheck.ANeedCheckLogin;
import com.liangwj.tools2k.apiServer.security.CommonAdminWebUser;
import com.liangwj.tools2k.beans.exceptions.BaseApiException;
import com.liangwj.tools2k.beans.form.IdForm;
import com.liangwj.tools2k.db.druid.beans.DruidInfoResponse;
import com.liangwj.tools2k.db.druid.beans.DruidListResponse;

@Service
@AApiServerImpl
@ConditionalOnClass(DruidDataSourceAutoConfigure.class)
@ANeedCheckLogin(userClass = CommonAdminWebUser.class)
public class CommonDruidImpl implements ICommonDruid {

	@Override
	public DruidInfoResponse getBasic() throws BaseApiException {
		final DruidInfoResponse res = new DruidInfoResponse();
		res.setInfo(DruidStatManagerFacade.getInstance().returnJSONBasicStat());
		return res;
	}

	@Override
	public DruidListResponse getDatasources() throws BaseApiException {
		final DruidListResponse res = new DruidListResponse();
		res.setList(DruidStatManagerFacade.getInstance().getDataSourceStatDataList());
		return res;
	}

	@Override
	public DruidListResponse getSqlByDatasourceId(IdForm form) throws BaseApiException {
		final DruidListResponse res = new DruidListResponse();
		res.setList(DruidStatManagerFacade.getInstance().getSqlStatDataList(form.getId()));
		return res;
	}

	@Override
	public DruidListResponse getWebUris() throws BaseApiException {
		final DruidListResponse res = new DruidListResponse();
		res.setList(WebAppStatManager.getInstance().getURIStatData());
		return res;
	}

	@Override
	public DruidListResponse getSessions() throws BaseApiException {
		final DruidListResponse res = new DruidListResponse();
		res.setList(WebAppStatManager.getInstance().getSessionStatData());
		return res;
	}

	@Override
	public void resetAll() throws BaseApiException {
		DruidStatManagerFacade.getInstance().resetAll();
	}

	@Override
	public DruidListResponse getWebApp() throws BaseApiException {
		final DruidListResponse res = new DruidListResponse();
		res.setList(WebAppStatManager.getInstance().getWebAppStatData());
		return res;
	}

}
