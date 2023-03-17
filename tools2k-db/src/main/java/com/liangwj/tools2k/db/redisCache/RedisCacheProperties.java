package com.liangwj.tools2k.db.redisCache;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <pre>
 * Cache 配置
 * 
 * 可在配置文件中配置每个cache的超时时间，格式:
 * 
 * cache.expire.map.[cache的名字]=时长（秒）
 * 
 * 例如：
 * cache.expire.map.myCache1=100
 * cache.expire.map.myCache2=200
 * 
 * 参数默认值：
 * cache.expire.defaultInSec=3600
 * cache.memoryCache.expireTimeInSec=3
 * cache.memoryCache.maxSize=1000
 * </pre>
 * 
 * @author rock 2016年8月11日
 */
@ConfigurationProperties(prefix = "cache")
public class RedisCacheProperties {

	private Expire expire = new Expire();

	private MemoryCache memoryCache = new MemoryCache();

	public static class Expire {
		/** 默认的超时时间, 默认值 5分钟 */
		private long defaultInSec = TimeUnit.MINUTES.toSeconds(5);

		public long getDefaultInSec() {
			return defaultInSec;
		}

		public void setDefaultInSec(long defaultInSec) {
			this.defaultInSec = defaultInSec;
		}

	}

	public static class MemoryCache {
		/** 内存cache的有效期 单位：3秒, 如果无需和其他项目共享redis，则时间可以长一点，例如10分钟1小时什么的 */
		private long expireTimeInSec = 3;
		/** 内存cache容量限制 */
		private int maxSize = 1000;

		public long getExpireTimeInSec() {
			return expireTimeInSec;
		}

		public void setExpireTimeInSec(long expireTimeInSec) {
			this.expireTimeInSec = expireTimeInSec;
		}

		public int getMaxSize() {
			return maxSize;
		}

		public void setMaxSize(int maxSize) {
			this.maxSize = maxSize;
		}

	}

	public Expire getExpire() {
		return expire;
	}

	public void setExpire(Expire expire) {
		this.expire = expire;
	}

	public MemoryCache getMemoryCache() {
		return memoryCache;
	}

	public void setMemoryCache(MemoryCache memoryCache) {
		this.memoryCache = memoryCache;
	}

}
