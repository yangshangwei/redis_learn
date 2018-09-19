package com.artisan.redis.baseStructure.strings;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

public class SpringRedisStringsDemo {

	public static void main(String[] args) {
		
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/spring-redis-string.xml");
		
		// 在 Spring 中，
		// redisTemplate.opsForValue（）所返回的对象可以操作简单的键值对，可以是字符串，也可以是对象，具体依据你所配置的序列化方案
		// 这里在spring-redis-string.xml中key和value都是指定的 stringRedisSerializer
		RedisTemplate<String,String> redisTemplate = (RedisTemplate<String, String>) ctx.getBean("redisTemplate");

		// 设置值
		//127.0.0.1:6379> set key1 value1
		//OK
		//127.0.0.1:6379> set key2 value2
		//OK
		redisTemplate.opsForValue().set("key1", "value1");
		redisTemplate.opsForValue().set("key2", "value2");
		
		// 通过 key 获取值
		//127.0.0.1:6379> get key1
		//"value1"
		String key1 = redisTemplate.opsForValue().get("key1");
		System.out.println("key1:" + key1);
		
		// 通过 key 删除值
		//127.0.0.1:6379> del key1
		//(integer) 1
		Boolean success = redisTemplate.delete("key1");
		System.out.println("删除key1是否成功：" + success);
		
		
		// 求长度
		//127.0.0.1:6379> strlen key2
		//(integer) 6
		Long size = redisTemplate.opsForValue().size("key2");
		System.out.println("key2长度：" + size);
		
		// 设值新值并返回旧值
		//127.0.0.1:6379> getset key2 new_value2
		//"value2"
		String oldValue = redisTemplate.opsForValue().getAndSet("key2", "new_value2");
		System.out.println("key2旧值：" + oldValue);

		
		// 127.0.0.1:6379> get key2
		// "new_value2"
		String newValue = redisTemplate.opsForValue().get("key2");
		System.out.println("key2新值：" + newValue);

		// 获取子串
		// 127.0.0.1:6379> getrange key2 0 3
		// "new_"
		String subString = redisTemplate.opsForValue().get("key2", 0, 3);
		System.out.println("subString：" + subString);

		// 将新的字符串value加入到原来 key指向的字符串末
		// 127.0.0.1:6379> append key2 _app
		// (integer) 14
		Integer value = redisTemplate.opsForValue().append("key2", "_app");
		System.out.println("value：" + value);

		// 127.0.0.1:6379> get key2
		// "new_value2_app"
		String newValue2 = redisTemplate.opsForValue().get("key2");
		System.out.println("key2：" + newValue2);
	}
}
