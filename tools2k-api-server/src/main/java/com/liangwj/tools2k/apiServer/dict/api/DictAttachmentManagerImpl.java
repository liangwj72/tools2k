package com.liangwj.tools2k.apiServer.dict.api;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.liangwj.tools2k.annotation.api.AApiServerImpl;
import com.liangwj.tools2k.apiServer.dict.DictCoreService;
import com.liangwj.tools2k.apiServer.dict.api.beans.DictAttachmentEditForm;
import com.liangwj.tools2k.apiServer.dict.api.beans.DictAttachmentSearchResponse;
import com.liangwj.tools2k.apiServer.dict.api.beans.DictAttachmentVo;
import com.liangwj.tools2k.apiServer.dict.api.beans.DictKeyForm;
import com.liangwj.tools2k.apiServer.loginCheck.ANeedCheckLogin;
import com.liangwj.tools2k.apiServer.security.CommonAdminWebUser;
import com.liangwj.tools2k.beans.exceptions.BaseApiException;
import com.liangwj.tools2k.beans.exceptions.SimpleApiException;

/**
 * <pre>
 * 字典管理的实现类
 * </pre>
 * 
 * @author rock 2016年11月16日
 */
@Service
@AApiServerImpl
@ANeedCheckLogin(userClass = CommonAdminWebUser.class)
public class DictAttachmentManagerImpl implements IDictAttechmentManager {

	@Autowired
	private DictCoreService coreService;

	@Override
	public DictAttachmentSearchResponse list() {

		List<DictAttachmentVo> list = this.coreService.getAllAttechmentsFromMap();

		DictAttachmentSearchResponse res = new DictAttachmentSearchResponse();
		res.setList(list);

		return res;
	}

	@Override
	public DictAttachmentSearchResponse save(DictAttachmentEditForm form) throws BaseApiException, IOException {

		if (!StringUtils.hasText(form.getKey())) {
			throw new SimpleApiException("键值不能为空");
		}

		// 保存
		this.coreService.saveAttachmentRow(form);

		// 返回列表结果，用于更新页面的列表页
		return this.list();
	}

	@Override
	public DictAttachmentSearchResponse delete(DictKeyForm form) throws BaseApiException, IOException {
		// 删除
		this.coreService.deleteAttachmengRow(form.getKey());

		return this.list();
	}
}
