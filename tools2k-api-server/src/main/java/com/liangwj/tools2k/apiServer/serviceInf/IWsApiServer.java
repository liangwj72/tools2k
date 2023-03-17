package com.liangwj.tools2k.apiServer.serviceInf;

import java.util.List;

import com.liangwj.tools2k.apiServer.beans.manager.IConnHandlerInfo;
import com.liangwj.tools2k.apiServer.beans.manager.WsConnectInfoBean;
import com.liangwj.tools2k.beans.others.IFilter;

/**
 * <pre>
 * web socket api server
 * </pre>
 * 
 * @author rock
 * 
 */
public interface IWsApiServer {

	List<WsConnectInfoBean> findConnectInfoBeanByFilter(IFilter<IConnHandlerInfo> filter);

	int getTotalConnectCount();

	int getTotalUserCount();

	void resetCounter();

	// 当运行记录历史信息时
	void onSaveHistroy();
}
