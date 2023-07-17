package com.liangwj.tools2k.apiServer.beans.manager;

import java.util.LinkedList;
import java.util.List;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.apiServer.security.UrlStatService.UrlBean;

import lombok.Getter;
import lombok.Setter;

/**
 * URL时间范围统计信息
 *
 * @author rock
 *
 */
@Setter
@Getter
public class UrlStatInfoBean {
	private final int id;
	@AComment("次数")
	private final long count;
	@AComment("时间范围 最大值")
	private final long timeRangeMax;
	@AComment("时间范围-最小值")
	private final long timeRangeMin;
	@AComment("URL历史记录")
	private final List<UrlBean> urlHistory = new LinkedList<>();

	public UrlStatInfoBean(int id, long timeRangeMin, long timeRangeMax, long count) {
		this.id = id;
		this.timeRangeMin = timeRangeMin;
		this.timeRangeMax = timeRangeMax;
		this.count = count;
	}

}
