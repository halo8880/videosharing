package com.example.videoauth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisConfig {
	@Value("${redis.host}")
	private String redisHost;
	@Value("${redis.port}")
	private int redisPort;
//	@Value("${redis.username}")
//	private int redisUsername;
//	@Value("${redis.pasword}")
//	private int redisPassword;

	@Bean
	JedisConnectionFactory jedisConnectionFactory(RedisStandaloneConfiguration configuration) {
		configuration.setPort(redisPort);
		configuration.setHostName(redisHost);
//		configuration.setUsername(redisHost);

		JedisConnectionFactory jedisConFactory
				= new JedisConnectionFactory(configuration);
//		jedisConFactory.setHostName(redisHost);
//		jedisConFactory.setPort(redisPort);

		return jedisConFactory;
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(jedisConnectionFactory);
		return template;
	}
}
