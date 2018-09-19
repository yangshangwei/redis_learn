package com.artisan.redis.baseStructure.strings;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

public class SpringRedisStringAdd_Subtraction {

	public static void main(String[] args) {

		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/spring-redis-string.xml");

		RedisTemplate redisTemplate = (RedisTemplate) ctx.getBean("redisTemplate");

		// 127.0.0.1:6379> set number 8
		// OK
		redisTemplate.opsForValue().set("number", String.valueOf(8));

		// 127.0.0.1:6379> GET number
		// "8"
		System.out.println(redisTemplate.opsForValue().get("number"));

		// 127.0.0.1:6379> INCR number
		// (integer) 9
		System.out.println(redisTemplate.opsForValue().increment("number", 1));

		// 127.0.0.1:6379> INCRBY number 5
		// (integer) 14
		System.out.println(redisTemplate.opsForValue().increment("number", 5));

		// 127.0.0.1:6379> DECR number
		// (integer) 13
		Long number = redisTemplate.getConnectionFactory().getConnection().decr(redisTemplate.getKeySerializer().serialize("number"));
		System.out.println(number);
		// 127.0.0.1:6379> DECRBY number 10
		// (integer) 3
		Long number2 = redisTemplate.getConnectionFactory().getConnection().decrBy(redisTemplate.getKeySerializer().serialize("number"), 10);
		System.out.println(number2);

		// 127.0.0.1:6379> INCRBYFLOAT number 8.88
		// "11.88"
		System.out.println(redisTemplate.opsForValue().increment("number", 8.88));
		// 127.0.0.1:6379> get number
		// "11.88"
		System.out.println(redisTemplate.opsForValue().get("number"));

	}

}
