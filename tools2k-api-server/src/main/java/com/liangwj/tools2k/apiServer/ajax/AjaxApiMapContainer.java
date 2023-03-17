package com.liangwj.tools2k.apiServer.ajax;

import java.lang.annotation.Annotation;

import org.springframework.stereotype.Service;

import com.liangwj.tools2k.annotation.api.AApiServerImpl;
import com.liangwj.tools2k.apiServer.beans.BaseResponse;
import com.liangwj.tools2k.apiServer.utils.BaseApiContainer;

/**
 * <pre>
 * 用于存储所有api的容器
 * </pre>
 * 
 * @author rock 2016年7月4日
 */
@Service
public class AjaxApiMapContainer extends BaseApiContainer<BaseResponse> {

	@Override
	protected Class<? extends Annotation> getAutoSearchAnnoClass() {
		return AApiServerImpl.class;
	}

}
