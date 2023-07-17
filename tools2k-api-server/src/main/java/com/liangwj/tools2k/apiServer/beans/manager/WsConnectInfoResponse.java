package com.liangwj.tools2k.apiServer.beans.manager;

import java.util.List;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.apiServer.beans.BaseResponse;

import lombok.Getter;
import lombok.Setter;

/** 返回所有ws连接信息 */
@Setter
@Getter
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

}
