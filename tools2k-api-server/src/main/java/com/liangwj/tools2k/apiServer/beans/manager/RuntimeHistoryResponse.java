package com.liangwj.tools2k.apiServer.beans.manager;

import java.util.List;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.apiServer.beans.BaseResponse;
import com.liangwj.tools2k.beans.system.DiskInfo;

/** 返回所有ws运行图表数据 */
public class RuntimeHistoryResponse extends BaseResponse {

	@AComment("磁盘信息")
	private DiskInfo diskInfo;

	@AComment("所有时间范围状态")
	private List<UrlStatInfoBean> uriStat;

	@AComment("图表数据")
	private List<RuntimeInfoBean> list;

	public List<RuntimeInfoBean> getList() {
		return list;
	}

	public void setList(List<RuntimeInfoBean> list) {
		this.list = list;
	}

	public DiskInfo getDiskInfo() {
		return diskInfo;
	}

	public void setDiskInfo(DiskInfo diskInfo) {
		this.diskInfo = diskInfo;
	}

	public List<UrlStatInfoBean> getUriStat() {
		return uriStat;
	}

	public void setUriStat(List<UrlStatInfoBean> uriStat) {
		this.uriStat = uriStat;
	}

}
