package com.liangwj.tools2k.apiServer.jmxInWeb.api;

import com.liangwj.tools2k.annotation.api.AClass;
import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.annotation.api.AMethod;
import com.liangwj.tools2k.apiServer.beans.CommonSuccessResponse;
import com.liangwj.tools2k.apiServer.jmxInWeb.api.beans.JwChangeAttrForm;
import com.liangwj.tools2k.apiServer.jmxInWeb.api.beans.JwInvokeOptForm;
import com.liangwj.tools2k.apiServer.jmxInWeb.api.beans.JwInvokeOptResponse;
import com.liangwj.tools2k.apiServer.jmxInWeb.api.beans.JwMBeanInfoResponse;
import com.liangwj.tools2k.apiServer.jmxInWeb.api.beans.JwMBeanListResponse;
import com.liangwj.tools2k.apiServer.jmxInWeb.api.beans.JwObjectNameForm;
import com.liangwj.tools2k.beans.exceptions.BaseApiException;

/**
 * <pre>
 * 字典管理用户接口
 * </pre>
 * 
 * @author rock 2016年11月16日
 */
@AClass("jmxInWeb")
@AComment(value = "JmxInWeb-JMX管理")
public interface IJmxInWebMBean {

	@AMethod(comment = "获取所有的MBean")
	JwMBeanListResponse getMBeanList() throws BaseApiException;

	@AMethod(comment = "改变一个属性")
	CommonSuccessResponse changeAttr(JwChangeAttrForm form) throws BaseApiException;

	@AMethod(comment = "调用一个方法")
	JwInvokeOptResponse invokeOpt(JwInvokeOptForm form) throws BaseApiException;

	@AMethod(comment = "获取一个MBean的详情")
	JwMBeanInfoResponse getMBeanInfo(JwObjectNameForm form) throws BaseApiException;

}
