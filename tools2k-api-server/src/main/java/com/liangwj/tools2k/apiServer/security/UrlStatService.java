package com.liangwj.tools2k.apiServer.security;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.stereotype.Service;

import com.liangwj.tools2k.annotation.api.AComment;
import com.liangwj.tools2k.apiServer.beans.manager.UrlStatInfoBean;
import com.liangwj.tools2k.beans.exceptions.IdNotFoundException;
import com.liangwj.tools2k.utils.other.QueueLinkedList;

/**
 * url请求统计
 *
 * <pre>
 *  0-20
 *  20-100
 *  100-300
 *  300-1000
 *  > 1000
 * </pre>
 *
 * @author rock
 *
 */
@Service
public class UrlStatService {

	/**
	 * 记录每个时间范围的URL访问数据
	 *
	 * @author rock
	 *
	 */
	public class StatInfo {
		private final int id;
		private final AtomicLong counter = new AtomicLong(); // 次数
		private final long timeRangeMax;// 时间范围 最大值
		private final long timeRangeMin;// 时间范围-最小值

		private final QueueLinkedList<UrlBean> urlLog = new QueueLinkedList<>(LOG_MAX_SIZE);
		private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

		public StatInfo(int id, long timeRangeMin, long timeRangeMax) {
			this.id = id;
			this.timeRangeMin = timeRangeMin;
			this.timeRangeMax = timeRangeMax;
		}

		/** 增加一条url记录 */
		void add(String url, long ms) {
			// 计时器 +1
			counter.incrementAndGet();

			this.lock.writeLock().lock();
			try {
				this.urlLog.add(new UrlBean(url, ms));
			} finally {
				this.lock.writeLock().unlock();
			}

		}

		/** 生成给api接口用的数据 */
		public UrlStatInfoBean toInfoBean(boolean includeUrl) {
			UrlStatInfoBean bean = new UrlStatInfoBean(this.id, this.timeRangeMin, this.timeRangeMax, this.counter.get());

			if (includeUrl) {
				// 如果要包含url详情
				this.lock.readLock().lock();
				try {
					bean.getUrlHistory().addAll(this.urlLog);

					Collections.reverse(bean.getUrlHistory()); // 反序，最新记录放前面
				} finally {
					this.lock.readLock().unlock();
				}
			}

			return bean;
		}

		/** 重置所有 */
		void reset() {
			// 计数器清零
			this.counter.set(0);

			// 清除历史
			this.lock.writeLock().lock();
			try {
				this.urlLog.clear();
			} finally {
				this.lock.writeLock().unlock();
			}
		}

	}

	/**
	 * 记录每个url的访问时间数据
	 *
	 * @author rock
	 *
	 */
	public class UrlBean {
		@AComment("访问时间")
		private final long accessTime;
		@AComment("消耗的时间：毫秒")
		private final long cost;
		@AComment("url")
		private final String url;

		public UrlBean(String url, long cost) {
			super();
			this.url = url;
			this.cost = cost;
			this.accessTime = System.currentTimeMillis();
		}

		public long getAccessTime() {
			return accessTime;
		}

		public long getCost() {
			return cost;
		}

		public String getUrl() {
			return url;
		}

	}

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UrlStatService.class);

	private static final int LOG_MAX_SIZE = 200; // 日志最大记录条数

	private final StatInfo[] stats; // 保存所有时间范围数据

	/** 时间分段 */
	private final int[] timeRanges = {
			20, // 第一个不能为0，编程方便
			100,
			300,
			1000
	};

	public UrlStatService() {
		// 将所有的数据范围数据都初始化好
		this.stats = new StatInfo[this.timeRanges.length + 1];
		int min = 0;
		int index = 0;
		for (int max : timeRanges) {
			this.stats[index] = new StatInfo(index, min, max);
			min = max;
			index++;
		}
		// 补上>1000的
		this.stats[this.stats.length - 1] = new StatInfo(index, min, -1);
		log.debug("初始化所有时间范围，合计 {} 个范围", index);
	}

	/**
	 * 添加一条统计记录
	 *
	 * @param url
	 * @param nanoTime
	 */
	public void add(String url, long nanoTime) {
		long ms = TimeUnit.NANOSECONDS.toMillis(nanoTime); // 转为毫秒

		// 找到要记录的地方
		int index = this.findIndex(ms);
		StatInfo stat = this.stats[index];

		stat.add(url, ms); // 增加记录

		log.debug("增加统计 url:{}, 时间:{}, index:{}", url, ms, index);
	}

	/** 根据毫秒时间，获取时间段的键值 */
	protected int findIndex(long ms) {
		int index = 0;
		for (; index < timeRanges.length; index++) {
			if (ms < this.timeRanges[index]) {
				// 小于范围的最大值，就是在这个范围内了，直接返回索引编号
				return index;
			}
		}

		// 如果都不匹配，就是在所有时间范围以外了，放最后一个
		return this.stats.length - 1;

	}

	/** 重置所有计数器 */
	public void reset() {
		for (StatInfo bean : stats) {
			bean.reset();
		}
	}

	/** 返回所有的时间范围统计数据 */
	public List<UrlStatInfoBean> getAllStat() {
		List<UrlStatInfoBean> list = new LinkedList<>();
		for (StatInfo info : stats) {
			// 列表时，不给url详情
			list.add(info.toInfoBean(false));
		}
		return list;
	}

	/** 根据下标，获取统计数据 */
	public UrlStatInfoBean getStatById(int index) throws IdNotFoundException {
		if (index < 0 || index >= this.stats.length) {
			throw new IdNotFoundException("没有这个时间范围的数据", index);
		}
		// 拿单个统计时，給url详情
		return this.stats[index].toInfoBean(true);
	}
}
