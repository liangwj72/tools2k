package com.liangwj.tools2k.apiServer.ajax.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.liangwj.tools2k.annotation.api.AApiServerImpl;
import com.liangwj.tools2k.apiServer.beans.CommonSuccessResponse;
import com.liangwj.tools2k.apiServer.beans.manager.OsInfoResponse;
import com.liangwj.tools2k.apiServer.beans.manager.RuntimeHistoryResponse;
import com.liangwj.tools2k.apiServer.beans.manager.UrlStatResponse;
import com.liangwj.tools2k.apiServer.beans.manager.WsConnectInfoBean;
import com.liangwj.tools2k.apiServer.beans.manager.WsConnectInfoResponse;
import com.liangwj.tools2k.apiServer.loginCheck.ANeedCheckLogin;
import com.liangwj.tools2k.apiServer.security.CommonAdminWebUser;
import com.liangwj.tools2k.apiServer.security.CounterService;
import com.liangwj.tools2k.apiServer.security.UrlStatService;
import com.liangwj.tools2k.apiServer.serviceInf.IWsApiServer;
import com.liangwj.tools2k.beans.exceptions.BaseApiException;
import com.liangwj.tools2k.beans.form.IdForm;
import com.liangwj.tools2k.beans.form.PageForm;
import com.liangwj.tools2k.utils.other.MBeanUtils;
import com.liangwj.tools2k.utils.other.OsUtil;

/**
 * <pre>
 * IJmxInWebWs接口的实现类
 * </pre>
 *
 * @author rock
 */
@Service
@AApiServerImpl
@ANeedCheckLogin(userClass = CommonAdminWebUser.class)
public class CommonRuntimeImpl implements ICommonRuntime {

	@Autowired
	private CounterService counterService;

	@Autowired(required = false)
	private IWsApiServer wsLoginContext;

	@Autowired
	private UrlStatService urlStatService;

	@Override
	public WsConnectInfoResponse wsConnectList(PageForm from) throws BaseApiException {
		if (this.wsLoginContext == null) {
			return new WsConnectInfoResponse();
		}

		// 所有连接信息, 这个list是ArrayList，可以按下标取值
		final List<WsConnectInfoBean> list = this.wsLoginContext.findConnectInfoBeanByFilter(null);

		// 根据页码获得起止范围
		int start = (from.getPageNo() - 1) * from.getPageSize();
		int end = start + from.getPageSize();

		// 防止超界
		if (end > list.size()) {
			end = list.size();
			start = end - from.getPageSize();
			if (start < 0) {
				start = 0;
			}
		}

		// 封装返回结果
		final WsConnectInfoResponse res = new WsConnectInfoResponse();

		// 流量信息
		res.setDownCounter(this.counterService.getWsDownCounter());
		res.setUpCounter(this.counterService.getWsUpCounter());

		// 连接信息
		res.setList(list.subList(start, end));

		// 在线信息
		res.setTotalConnectCount(this.wsLoginContext.getTotalConnectCount());
		res.setTotalUserCount(this.wsLoginContext.getTotalUserCount());

		return res;
	}

	@Override
	public RuntimeHistoryResponse runtimeHistory() throws BaseApiException {
		final RuntimeHistoryResponse res = new RuntimeHistoryResponse();
		res.setUriStat(this.urlStatService.getAllStat()); // 访问时长段统计
		res.setDiskInfo(OsUtil.getDiskInfo()); // 硬盘统计
		res.setList(this.counterService.getRuntimeHistory()); // 图表数据
		return res;
	}

	@Override
	public OsInfoResponse osInfo() throws BaseApiException {
		final OsInfoResponse res = new OsInfoResponse();

		res.setOs(MBeanUtils.getOsInfo());
		res.setClassLoading(MBeanUtils.getClassLoadingInfo());
		res.setThreading(MBeanUtils.getThreadingInfo());
		res.setVm(MBeanUtils.getVmInfo());

		return res;
	}

	@Override
	public CommonSuccessResponse wsResetCounter() throws BaseApiException {

		if (this.wsLoginContext != null) {
			this.wsLoginContext.resetCounter();
		}

		return CommonSuccessResponse.DEFAULT;
	}

	@Override
	public UrlStatResponse getUrlStatById(IdForm form) throws BaseApiException {
		UrlStatResponse res = new UrlStatResponse();
		res.setStat(this.urlStatService.getStatById(form.getId()));
		return res;
	}
}
