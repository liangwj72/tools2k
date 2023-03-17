package com.liangwj.tools2k.db.redisCache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.util.Assert;

import com.liangwj.tools2k.annotation.api.ADomainOrder;
import com.liangwj.tools2k.utils.spring.CommonMBeanDomainNaming;

/**
 * <pre>
 * 用于异步put cache的CacheManager
 * </pre>
 *
 * @author rock 2016年8月24日
 */
@ManagedResource(description = "异步Cache管理器")
@ADomainOrder(order = CommonMBeanDomainNaming.ORDER, domainName = CommonMBeanDomainNaming.DOMAIN)
public class ASyncCacheManager implements CacheManager {
	/**
	 * <pre>
	 * 自定义的Cache，用于将各类方法都通过asyncCacheExecuter进行异步执行
	 * </pre>
	 *
	 * @author rock 2016年8月24日
	 */
	public class MyCache implements Cache {

		private final Cache originCache;

		public MyCache(Cache originCache) {
			super();
			this.originCache = originCache;
		}

		@Override
		public void clear() {
			threadpool.addNewTask(() -> {
				ASyncCacheManager.this.asyncCacheExecuter.asyncClear(originCache);
			});
		}

		@Override
		public void evict(Object key) {
			threadpool.addNewTask(() -> {
				ASyncCacheManager.this.asyncCacheExecuter.asyncEvict(originCache, key);
			});
		}

		@Override
		public ValueWrapper get(Object key) {
			return ASyncCacheManager.this.asyncCacheExecuter.get(originCache, key);
		}

		@Override
		public <T> T get(Object key, Class<T> type) {
			return ASyncCacheManager.this.asyncCacheExecuter.get(originCache, key, type);
		}

		@Override
		public String getName() {
			return this.originCache.getName();
		}

		@Override
		public Object getNativeCache() {
			return this.originCache.getNativeCache();
		}

		@Override
		public void put(Object key, Object value) {
			// 现将数据放到内存
			ASyncCacheManager.this.asyncCacheExecuter.putToMemoryCache(this, key, value);

			threadpool.addNewTask(() -> {
				// 再执行异步的put操作
				ASyncCacheManager.this.asyncCacheExecuter.asyncPut(originCache, key, value);
			});
		}

		@Override
		public ValueWrapper putIfAbsent(Object key, Object value) {
			return ASyncCacheManager.this.asyncCacheExecuter.putIfAbsent(originCache, key, value);
		}

		@Override
		public <T> T get(Object key, Callable<T> callable) {
			return this.originCache.get(key, callable);
		}
	}

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ASyncCacheManager.class);

	/**
	 * 异步执行器
	 */
	@Autowired
	private IASyncCacheExecuter asyncCacheExecuter;

	@Autowired
	private RedisCacheThreadPool threadpool;

	/** 保存了所有 cache 的 map */
	private final Map<String, MyCache> cacheMap = new HashMap<>();

	/** 原来redisCacheManager */
	private final RedisCacheManager redisCacheManager;

	public ASyncCacheManager(RedisCacheManager redisCacheManager) {
		super();

		Assert.notNull(redisCacheManager, "redisCacheManager 不能为空");
		this.redisCacheManager = redisCacheManager;
	}

	@ManagedOperation(description = "清空所有Cache")
	public String clearAllCache() {
		final Collection<String> all = this.getCacheNames();
		for (final String name : all) {
			final Cache cache = this.getCache(name);
			cache.clear();
		}
		return "clear所有的Cache完成";
	}

	@ManagedOperation(description = "清空指定的Cache")
	@ManagedOperationParameters({
			@ManagedOperationParameter(description = "要清空的Cache的名字", name = "cacheName")
	})
	public String clearCache(String cacheName) {
		if (this.getCacheNames().contains(cacheName)) {
			final Cache cache = this.getCache(cacheName);
			cache.clear();
			return "clear完成";
		} else {
			return "无此Cache";
		}
	}

	@ManagedAttribute(description = "查看所有的Cache")
	public Collection<String> getAllCacheName() {
		final Collection<String> all = this.getCacheNames();
		return all;
	}

	@Override
	public Cache getCache(String name) {
		MyCache cache = this.cacheMap.get(name);
		if (cache == null) {

			log.debug("Cache({}) 尚未创建，创建新Cache", name);

			final Cache originCache = this.redisCacheManager.getCache(name);
			cache = new MyCache(originCache);

			this.cacheMap.put(name, cache);
		}

		return cache;
	}

	@Override
	public Collection<String> getCacheNames() {
		return this.redisCacheManager.getCacheNames();
	}

	@ManagedAttribute(description = "默认的Cache超时时间")
	public Object getDefaultExpiration() {
		return this.redisCacheManager.getCacheConfigurations();
	}

}
