
package com.liangwj.tools2k.db.redisCache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

/**
 * <pre>
 * redis的相关配置
 * </pre>
 * 
 * @author rock 2016年8月15日
 */
@Configuration
public class RedisAutoConfig {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RedisAutoConfig.class);

	public RedisAutoConfig() {
		log.info("自动配置 Redis");
	}

	@Bean
	public RedisTemplate<String, ?> redisTemplate(RedisConnectionFactory factory) {

		log.debug("创建 RedisTemplate<String, Object>");

		final StringRedisTemplate template = new StringRedisTemplate(factory);

		// 设置对象序列化工具，可以使得要缓存的对象不需要实现Serializable接口
		// template.setValueSerializer(this.getRedisSerializer());
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		template.afterPropertiesSet();

		return template;
	}

	/**
	 * value的序列号工具，用json格式的，客户端查看的时候会方便点。
	 * 
	 * @return
	 */
	// private RedisSerializer<?> getRedisSerializer() {
	// final Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
	// new Jackson2JsonRedisSerializer<>(
	// Object.class);
	// final ObjectMapper om = new ObjectMapper();
	// om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
	// om.activateDefaultTyping(om.getPolymorphicTypeValidator(),
	// DefaultTyping.NON_FINAL);
	// jackson2JsonRedisSerializer.setObjectMapper(om);
	//
	// return jackson2JsonRedisSerializer;
	// }
}
