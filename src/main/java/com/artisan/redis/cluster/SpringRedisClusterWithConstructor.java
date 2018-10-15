package com.artisan.redis.cluster;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class SpringRedisClusterWithConstructor {

	@SuppressWarnings({ "rawtypes", "unchecked", "resource" })
	public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/spring-redis-cluster_constructor.xml");
        //获得JedisClient对象
        JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
        jedisClient.set("A-XXX", "test-1xxx");
        System.out.println(jedisClient.get("A-XXX"));
	}
}
