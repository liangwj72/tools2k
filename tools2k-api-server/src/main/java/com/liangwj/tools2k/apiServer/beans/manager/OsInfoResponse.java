package com.liangwj.tools2k.apiServer.beans.manager;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.apiServer.beans.BaseResponse;
import com.liangwj.tools2k.beans.system.ClassLoadingInfo;
import com.liangwj.tools2k.beans.system.OsInfo;
import com.liangwj.tools2k.beans.system.ThreadingInfo;
import com.liangwj.tools2k.beans.system.VmInfo;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 服务器硬件和虚拟机信息
 * </pre>
 * 
 * @author rock
 * 
 */
@Setter
@Getter
public class OsInfoResponse extends BaseResponse {

	@AComment("操作系统信息")
	private OsInfo os;

	@AComment("类加载信息")
	private ClassLoadingInfo classLoading;

	@AComment("线程信息")
	private ThreadingInfo threading;

	@AComment("虚拟机信息")
	private VmInfo vm;

}
