package com.artisan.redis.baseStructure.hyperloglgo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;



public class SpringRedisHyperLogLogDemo {

	@SuppressWarnings({ "unchecked", "rawtypes", "resource" })
	public static void main(String[] args) {
		
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/spring-redis-hyperloglog.xml");

		RedisTemplate redisTemplate = (RedisTemplate) ctx.getBean("redisTemplate");

		// 为确保数据干净，先清除
		redisTemplate.delete("h1");
		redisTemplate.delete("h2");
		redisTemplate.delete("h3");

		// 添加指定元素到 HyperLogLog 中
		Long count = redisTemplate.opsForHyperLogLog().add("h1", "a", "b", "c", "d", "a");
		System.out.println(count);
		count = redisTemplate.opsForHyperLogLog().add("h2", "a");
		System.out.println(count);
		count = redisTemplate.opsForHyperLogLog().add("h2", "z");
		System.out.println(count);

		Long size = redisTemplate.opsForHyperLogLog().size("h1");
		System.out.println(size);
		Long size2 = redisTemplate.opsForHyperLogLog().size("h2");
		System.out.println(size2);

		Long size3 = redisTemplate.opsForHyperLogLog().union("h3", "h1", "h2");
		System.out.println(size3);

		Long size4 = redisTemplate.opsForHyperLogLog().size("h3");
		System.out.println(size4);
	}
	
}