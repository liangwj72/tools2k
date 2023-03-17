package com.liangwj.tools2k.apiServer.ajax.api.beans;

import java.util.List;

import com.liangwj.tools2k.annotation.api.AComment;

@AComment("对一个接口的描述")
public class ApiMethodBean {

	@AComment("接口方法名")
	private String methodKey;
	@AComment("无前缀的接口路径")
	private String url;
	@AComment("备注")
	private String memo;
	@AComment("无前缀的接口路径")
	private String key;
	@AComment("接口的唯一标识符")
	private boolean needLogin;
	@AComment("需要的权限id")
	private String optId;
	@AComment("需要检查的用户类型")
	private String webUserClasses;
	@AComment("是否有上传文件")
	private boolean uploadFile;
	@AComment("返回的类名")
	private String returnClassName;
	@AComment("返回的类的说明")
	private String returnClassDesc;
	@AComment("方法的参数列表")
	private List<ApiParamBean> paramVoList;

	public String getMethodKey() {
		return methodKey;
	}

	public void setMethodKey(String methodKey) {
		this.methodKey = methodKey;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isNeedLogin() {
		return needLogin;
	}

	public void setNeedLogin(boolean needLogin) {
		this.needLogin = needLogin;
	}

	public String getOptId() {
		return optId;
	}

	public void setOptId(String optId) {
		this.optId = optId;
	}

	public String getWebUserClasses() {
		return webUserClasses;
	}

	public void setWebUserClasses(String webUserClasses) {
		this.webUserClasses = webUserClasses;
	}

	public boolean isUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(boolean uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getReturnClassName() {
		return returnClassName;
	}

	public void setReturnClassName(String returnClassName) {
		this.returnClassName = returnClassName;
	}

	public String getReturnClassDesc() {
		return returnClassDesc;
	}

	public void setReturnClassDesc(String returnClassDesc) {
		this.returnClassDesc = returnClassDesc;
	}

	public List<ApiParamBean> getParamVoList() {
		return paramVoList;
	}

	public void setParamVoList(List<ApiParamBean> paramVoList) {
		this.paramVoList = paramVoList;
	}

}
