package com.artisan.redis.publish;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

public class PublishSubscribeTest {

	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/spring-redis-publish.xml");
		RedisTemplate redisTemplate = ctx.getBean(RedisTemplate.class);

		String channel = "talk";
		redisTemplate.convertAndSend(channel, "artisan-talk");
	}
}
