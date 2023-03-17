package com.liangwj.tools2k.apiServer.jmxInWeb.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.liangwj.tools2k.annotation.api.AApiServerImpl;
import com.liangwj.tools2k.apiServer.beans.CommonSuccessResponse;
import com.liangwj.tools2k.apiServer.jmxInWeb.JmxInWebService;
import com.liangwj.tools2k.apiServer.jmxInWeb.api.beans.JwChangeAttrForm;
import com.liangwj.tools2k.apiServer.jmxInWeb.api.beans.JwInvokeOptForm;
import com.liangwj.tools2k.apiServer.jmxInWeb.api.beans.JwInvokeOptResponse;
import com.liangwj.tools2k.apiServer.jmxInWeb.api.beans.JwMBeanInfoResponse;
import com.liangwj.tools2k.apiServer.jmxInWeb.api.beans.JwMBeanListResponse;
import com.liangwj.tools2k.apiServer.jmxInWeb.api.beans.JwObjectNameForm;
import com.liangwj.tools2k.apiServer.jmxInWeb.api.beans.MBeanVo;
import com.liangwj.tools2k.apiServer.loginCheck.ANeedCheckLogin;
import com.liangwj.tools2k.apiServer.security.CommonAdminWebUser;
import com.liangwj.tools2k.beans.exceptions.BaseApiException;

/**
 * <pre>
 * 接口的实现类
 * </pre>
 * 
 * @author rock 2017年4月25日
 */
@Service
@AApiServerImpl
public class JmxInWebMBeanImpl implements IJmxInWebMBean {

	@Autowired
	private JmxInWebService service;

	@Override
	@ANeedCheckLogin(userClass = CommonAdminWebUser.class)
	public JwMBeanListResponse getMBeanList() throws BaseApiException {
		// 封装返回结果
		JwMBeanListResponse res = new JwMBeanListResponse();
		res.setList(this.service.getMBeanList());
		return res;
	}

	@Override
	@ANeedCheckLogin(userClass = CommonAdminWebUser.class)
	public CommonSuccessResponse changeAttr(JwChangeAttrForm form) throws BaseApiException {

		this.service.changeAttr(form);

		return CommonSuccessResponse.DEFAULT;
	}

	@Override
	@ANeedCheckLogin(userClass = CommonAdminWebUser.class)
	public JwInvokeOptResponse invokeOpt(JwInvokeOptForm form) throws BaseApiException {
		return this.service.invokeOpt(form.getObjectName(), form.getOptName(), form.getParamInfo());
	}

	@Override
	@ANeedCheckLogin(userClass = CommonAdminWebUser.class)
	public JwMBeanInfoResponse getMBeanInfo(JwObjectNameForm form) throws BaseApiException {
		MBeanVo vo = this.service.getMBeanInfo(form.getObjectName());

		JwMBeanInfoResponse res = new JwMBeanInfoResponse();
		res.setInfo(vo);
		return res;
	}

}
