package com.liangwj.tools2k.apiServer.websocket;

import java.lang.annotation.Annotation;

import org.springframework.stereotype.Service;

import com.liangwj.tools2k.annotation.api.WsApiImpl;
import com.liangwj.tools2k.apiServer.beans.BaseSocketResponse;
import com.liangwj.tools2k.apiServer.utils.BaseApiContainer;

/**
 * 为爬虫提供的Api容器
 * 
 * 所有配置了 {@link WsApiImpl} 这个标注的Api实现都会添加进这个容器
 * 
 * @author lxy
 *
 */
@Service
public class WsApiMapContainer extends BaseApiContainer<BaseSocketResponse> {

	@Override
	protected Class<? extends Annotation> getAutoSearchAnnoClass() {
		return WsApiImpl.class;
	}
}
