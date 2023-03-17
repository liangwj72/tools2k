package com.liangwj.tools2k.apiServer.ajax.api.beans;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.apiServer.beans.BaseResponse;
import com.liangwj.tools2k.apiServer.utils.ApiMethodInfo;

/**
 * <pre>
 * 用于开发界面的响应
 * </pre>
 * 
 * @author rock 2016年7月4日
 */
public class DevPageResponse extends BaseResponse {

	@AComment("url的前缀")
	private final String apiUrlPrefix;
	@AComment("所有的接口分组")
	private final List<ApiGroupBean> infGroup = new LinkedList<>();

	public DevPageResponse(String apiUrlPrefix, List<ApiMethodInfo<?>> methods) {
		super();
		this.apiUrlPrefix = apiUrlPrefix;

		Map<String, ApiGroupBean> groupMap = new HashMap<>();
		for (ApiMethodInfo<?> mi : methods) {
			String key = mi.getInfaceKey();

			// 看看是否存在这个分组
			ApiGroupBean group = groupMap.get(key);
			if (group == null) {
				// 如果不存在就添加
				group = new ApiGroupBean();
				group.setInfKey(key);
				group.setMemo(mi.getInfMemo());
				groupMap.put(key, group);

				// 同时将分组放到列表中
				this.infGroup.add(group);
			}
			// 将方法放到分组中
			group.addMethod(mi.getInfoBean());
		}

		Collections.sort(this.infGroup);

	}

	public List<ApiGroupBean> getInfGroup() {
		return infGroup;
	}

	public String getApiUrlPrefix() {
		return apiUrlPrefix;
	}

}
