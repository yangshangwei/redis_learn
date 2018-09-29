package com.artisan.redis.sentinel;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;

public class SpringRedisSentinel {

	@SuppressWarnings({ "unchecked", "rawtypes", "resource" })
	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/spring-redis-sentinel.xml");
		RedisTemplate redisTemplate = ctx.getBean(RedisTemplate.class);

		String retVaule = (String) redisTemplate.execute((RedisOperations ops) -> {
			ops.boundValueOps("artisan_k").set("artisan_v");
			String value = (String) ops.boundValueOps("artisan_k").get();
			return value;
		});

		System.out.println(retVaule);
	}

}
