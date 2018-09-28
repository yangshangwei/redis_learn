package com.artisan.redis.expire;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;

public class SpringRedisExpireDemo {

	@SuppressWarnings({ "unchecked", "rawtypes", "resource" })
	public static void main(String[] args) {

		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/spring-redis-string.xml");
		RedisTemplate redisTemplate = ctx.getBean(RedisTemplate.class);

		redisTemplate.execute((RedisOperations ops) -> {

			ops.boundValueOps("key1").set("value1");
			String value = (String) ops.boundValueOps("key1").get();
			System.out.println("value=" + value);

			long expSecond = ops.getExpire("key1");
			System.out.println("expSecond:" + expSecond);

			// 设置120秒
				Boolean flag = ops.expire("key1", 120L, TimeUnit.SECONDS);
				System.out.println("设置超时时间：" + flag);
				System.out.println("过期时间：" + ops.getExpire("key1") + "秒");

				// 持久化 key，取消超时时间
				flag = ops.persist("key1");
				System.out.println("取消超时时间：" + flag);
				System.out.println("过期时间：" + ops.getExpire("key1"));

				Date date = new Date();
				date.setTime(System.currentTimeMillis() + 120000);
				// 设置超时时间点
				flag = ops.expireAt("key1", date);
				System.out.println("设置超时时间：" + flag);
				System.out.println("过期时间：" + ops.getExpire("key1"));
				return null;
			});
	}

}
