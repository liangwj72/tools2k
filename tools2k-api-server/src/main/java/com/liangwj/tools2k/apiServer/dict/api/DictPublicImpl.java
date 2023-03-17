package com.liangwj.tools2k.apiServer.dict.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.liangwj.tools2k.annotation.api.AApiServerImpl;
import com.liangwj.tools2k.apiServer.debugMode.DebugModeProperties;
import com.liangwj.tools2k.apiServer.dict.DictCoreService;
import com.liangwj.tools2k.apiServer.dict.api.beans.DictPublicResponse;

/**
 * <pre>
 * 字典公开接口实现类
 * </pre>
 * 
 * @author rock
 */
@Service
@AApiServerImpl
public class DictPublicImpl implements IDictPublic {

	@Autowired
	private DictCoreService coreService;

	@Autowired
	private DebugModeProperties debugMode;

	@Override
	public DictPublicResponse getDict() {
		DictPublicResponse res = new DictPublicResponse();
		res.setData(this.coreService.toJsonMap());
		res.setDebugMode(this.debugMode.isDebugMode());
		return res;
	}
}
