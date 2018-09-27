package com.artisan.redis.transaction;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

public class SpringRedisTransaction {

	@SuppressWarnings({ "unchecked", "rawtypes", "resource" })
	public static void main(String[] args) {

		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/spring-redis-string.xml");
		RedisTemplate<String, String> redisTemplate = ctx.getBean(RedisTemplate.class);
		
		// 清掉key
		redisTemplate.delete("key1");
		
		SessionCallback sessionCallback = (SessionCallback) (RedisOperations ops) -> {
			// 开启事务
			ops.multi();
			// 设置值
			ops.boundValueOps("key1").set("artisan");
			// 注意由于命令只是进入队列 ，而没有被执行，所以此处采用 get 命令 ，而 value 却返回为null
			String value = (String) ops.boundValueOps("key1").get();
			System.out.println("事务执行过程中 ， 命令入队列，而没有被执行，所以 value 为空 ：value=" + value);

			// 此时 list 会保存之前进入队列的所有命令的结果
			List list = ops.exec();
			for (int i = 0; i < list.size(); i++) {
				System.out.println("队列中的命令返回的结果：" + list.get(i).toString());
			}

			// 事务结束后 ， 获取 valuel
			value = (String) redisTemplate.opsForValue().get("key1");
			System.out.println("----:" + value);
			return value;
		};

		// 执行Redis命令
		String value = (String) redisTemplate.execute(sessionCallback);
		System.out.println("value:" + value);
	}

}
