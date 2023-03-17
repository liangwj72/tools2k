package com.liangwj.tools2k.apiServer.dict.api;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson2.JSON;
import com.liangwj.tools2k.annotation.api.AApiServerImpl;
import com.liangwj.tools2k.apiServer.beans.CommonSuccessResponse;
import com.liangwj.tools2k.apiServer.dict.DictCoreService;
import com.liangwj.tools2k.apiServer.dict.DictXml;
import com.liangwj.tools2k.apiServer.dict.api.beans.DictImportForm;
import com.liangwj.tools2k.apiServer.dict.api.beans.DictKeyForm;
import com.liangwj.tools2k.apiServer.dict.api.beans.DictKeySearchResponse;
import com.liangwj.tools2k.apiServer.dict.api.beans.DictRowEditForm;
import com.liangwj.tools2k.apiServer.dict.api.beans.DictVo;
import com.liangwj.tools2k.apiServer.dict.api.beans.KeySearchPageForm;
import com.liangwj.tools2k.apiServer.loginCheck.ANeedCheckLogin;
import com.liangwj.tools2k.apiServer.security.CommonAdminWebUser;
import com.liangwj.tools2k.beans.exceptions.BaseApiException;
import com.liangwj.tools2k.beans.exceptions.SimpleApiException;
import com.liangwj.tools2k.beans.exceptions.SystemErrorException;
import com.liangwj.tools2k.beans.others.PageQueryResult;
import com.liangwj.tools2k.utils.other.FileUtil;
import com.liangwj.tools2k.utils.spring.WebContextHolderHelper;
import com.liangwj.tools2k.utils.web.BinderUtil;

/**
 * <pre>
 * 字典管理的实现类
 * </pre>
 * 
 * @author rock 2016年11月16日
 */
@Service
@ANeedCheckLogin(userClass = CommonAdminWebUser.class)
@AApiServerImpl
public class DictManagerImpl implements IDictManager {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DictManagerImpl.class);

	private final String sessionName = this.getClass().getName();

	@Autowired
	private DictCoreService coreService;

	@Override
	public DictKeySearchResponse search(KeySearchPageForm form) {

		PageQueryResult<DictVo> page = this.coreService.findInPage(form);

		DictKeySearchResponse res = new DictKeySearchResponse();

		res.setItemTotal(page.getItemTotal());
		res.setList(page.getList());
		res.setPageNo(page.getPageNo());
		res.setPageSize(page.getPageSize());
		res.setPageTotal(page.getPageTotal());

		return res;
	}

	protected DictKeySearchResponse getListFromSessionForm() throws BaseApiException {
		KeySearchPageForm form = BinderUtil.getFormFromSession(
				WebContextHolderHelper.getRequest(), KeySearchPageForm.class, sessionName);

		return this.search(form);
	}

	@Override
	@ANeedCheckLogin(userClass = CommonAdminWebUser.class)
	public DictKeySearchResponse save(DictRowEditForm form) throws BaseApiException {

		if (!StringUtils.hasText(form.getKey())) {
			throw new SimpleApiException("键值不能为空");
		}

		// 保存
		this.coreService.saveRow(form);

		// 返回列表结果，用于更新页面的列表页
		return this.getListFromSessionForm();
	}

	@Override
	@ANeedCheckLogin(userClass = CommonAdminWebUser.class)
	public CommonSuccessResponse importXml(DictImportForm form) throws BaseApiException {

		MultipartFile multipartFile = form.getFile();

		if (multipartFile == null || multipartFile.isEmpty()) {
			throw new SimpleApiException("请上传文件");
		}

		try {
			String xmlStr = FileUtil.readFile(multipartFile.getInputStream());

			DictXml xml = JSON.parseObject(xmlStr, DictXml.class);

			if (xml.getDictXmlRow().isEmpty()) {
				throw new SimpleApiException("上传的xml文件中没有数据");
			}

			log.debug("收到上传的xml文件, 含{}条记录 ", xml.getDictXmlRow().size());

			this.coreService.importXml(xml, form.isCleanOld());

		} catch (IOException e) {
			throw new SystemErrorException(e);
		}

		return CommonSuccessResponse.DEFAULT;
	}

	@Override
	public DictKeySearchResponse delete(DictKeyForm form) throws BaseApiException {
		this.coreService.deleteRow(form.getKey());
		return this.getListFromSessionForm();
	}

}
