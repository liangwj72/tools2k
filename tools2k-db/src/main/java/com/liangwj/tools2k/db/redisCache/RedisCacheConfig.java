
package com.liangwj.tools2k.db.redisCache;

import java.lang.reflect.Method;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * <pre>
 * 用 redis 作为cache 的相关配置
 * </pre>
 *
 * @author rock 2016年8月15日
 */
@Configuration
@EnableConfigurationProperties(RedisCacheProperties.class)
@Import(value = {
		RedisAutoConfig.class
})
public class RedisCacheConfig {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RedisCacheConfig.class);

	public RedisCacheConfig() {
		log.info("自动配置 Redis Cache");
	}

	@Autowired
	private RedisCacheProperties prop;

	@Bean
	public ASyncCacheExecuterCounterMBean asyncCacheExecuterCounterMBean() {
		return new ASyncCacheExecuterCounterMBean();
	}

	@Bean
	public RedisCacheThreadPool redisCacheThreadPool() {
		return new RedisCacheThreadPool();
	}

	/**
	 * 查看异步执行cache功能的的管理器，同时也是二级缓存
	 *
	 * @return
	 */
	@Bean
	public IASyncCacheExecuter asyncCacheExecuter() {
		return new ASyncCacheExecuter(this.prop.getMemoryCache().getExpireTimeInSec(),
				this.prop.getMemoryCache().getMaxSize());
	}

	@Bean
	public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {

		RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);

		// 设置CacheManager的值序列化方式为json序列化
		RedisSerializer<Object> jsonSerializer = new GenericJackson2JsonRedisSerializer();
		RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair
				.fromSerializer(jsonSerializer);
		RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
				.serializeValuesWith(pair);
		// 设置缓存有效期
		defaultCacheConfig.entryTtl(Duration.ofSeconds(this.prop.getExpire().getDefaultInSec()));
		// 初始化RedisCacheManager
		RedisCacheManager redisCm = new RedisCacheManager(redisCacheWriter, defaultCacheConfig);

		final ASyncCacheManager asyncCm = new ASyncCacheManager(redisCm);

		return asyncCm;
	}

	/**
	 * 用于根据方法中的参数生成key的生成器
	 *
	 * @return
	 */
	@Bean
	public KeyGenerator commonKeyGenerator() {
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method, Object... params) {
				final StringBuilder sb = new StringBuilder();
				sb.append(target.getClass().getSimpleName());
				sb.append(method.getName());
				for (final Object obj : params) {
					if (obj instanceof Pageable) {
						final Pageable page = (Pageable) obj;
						sb.append(String.format("_page:%d_%d_", page.getPageSize(), page.getPageNumber()));
					} else {
						sb.append(obj);
					}
				}

				final String key = sb.toString();

				log.debug("Cache 的key={}", key);

				return key;
			}
		};
	}
}
