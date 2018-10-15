package com.artisan.redis.cluster;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;


public class SpringRedisCluster {

	@SuppressWarnings({ "rawtypes", "unchecked", "resource" })
	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/spring-redis-cluster.xml");
		
		RedisTemplate redisTemplate = (RedisTemplate) ctx.getBean(RedisTemplate.class);
		System.out.println("mykey的value是:" + redisTemplate.opsForValue().get("mykey"));
		
		redisTemplate.boundValueOps("xkey").set("xvalue");
		System.out.println(redisTemplate.opsForValue().get("xkey"));
	}
	
}
