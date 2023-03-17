package com.liangwj.tools2k.apiServer.beans.manager;

import java.util.List;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.apiServer.beans.BaseResponse;

/** 返回所有ws连接信息 */
public class WsConnectInfoResponse extends BaseResponse {

	@AComment("上行使用情况")
	private PayloadCounter upCounter;

	@AComment("下行使用情况")
	private PayloadCounter downCounter;

	@AComment("连接总数")
	private int totalConnectCount;

	@AComment("在线用户总数")
	private int totalUserCount;

	@AComment("web socket 连接信息")
	private List<WsConnectInfoBean> list;

	public List<WsConnectInfoBean> getList() {
		return list;
	}

	public void setList(List<WsConnectInfoBean> list) {
		this.list = list;
	}

	public int getTotalConnectCount() {
		return totalConnectCount;
	}

	public void setTotalConnectCount(int totalConnectCount) {
		this.totalConnectCount = totalConnectCount;
	}

	public int getTotalUserCount() {
		return totalUserCount;
	}

	public void setTotalUserCount(int totalUserCount) {
		this.totalUserCount = totalUserCount;
	}

	public PayloadCounter getUpCounter() {
		return upCounter;
	}

	public void setUpCounter(PayloadCounter upCounter) {
		this.upCounter = upCounter;
	}

	public PayloadCounter getDownCounter() {
		return downCounter;
	}

	public void setDownCounter(PayloadCounter downCounter) {
		this.downCounter = downCounter;
	}

}
