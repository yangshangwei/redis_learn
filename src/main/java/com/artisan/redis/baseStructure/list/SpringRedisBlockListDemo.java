package com.artisan.redis.baseStructure.list;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;


public class SpringRedisBlockListDemo {
	
	private static final String KEY1 = "list1";
	private static final String KEY2 = "list2";
	
	public static void main(String[] args) {
		
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/spring-redis-list.xml");
		RedisTemplate redisTemplate = ctx.getBean(RedisTemplate.class);

		// 清空操作
		redisTemplate.delete(KEY1);
		redisTemplate.delete(KEY2);
		
		// 127.0.0.1:6379> LPUSH list node1 node2 node3 node4 node5
		// (integer) 5
		
		List<String> list = new ArrayList<String>();
		for (int i = 1; i <= 5; i++) {
			list.add("node" + i);
		}
		redisTemplate.opsForList().leftPushAll(KEY1, list);
		scanList(redisTemplate, KEY1, 0, 4);
		System.out.println("---------------------------");

		// 127.0.0.1:6379> BLPOP list 2
		// 1) "list"
		// 2) "node5"
		// Spring 使用参数超时时间作为阻塞命令区分，等价于 blpop 命令，并且可以设置时间参数
		String lefpPodNode = (String) redisTemplate.opsForList().leftPop(KEY1, 2, TimeUnit.SECONDS);
		System.out.println("leftPopNode:" + lefpPodNode);
		System.out.println("---------------------------");

		// 127.0.0.1:6379> LRANGE list 0 4
		// 1) "node4"
		// 2) "node3"
		// 3) "node2"
		// 4) "node1"
		scanList(redisTemplate, KEY1, 0, 4);
		System.out.println("---------------------------");

		// 127.0.0.1:6379> BRPOP list 3
		// 1) "list"
		// 2) "node1"
		// Spring 使用参数超时时间作为阻塞命令区分，等价于 brpop 命令，并且可以设置时间参数
		System.out.println("rightPopNode:" + redisTemplate.opsForList().rightPop(KEY1, 3, TimeUnit.SECONDS));
		System.out.println("---------------------------");

		// 127.0.0.1:6379> LRANGE list 0 4
		// 1) "node4"
		// 2) "node3"
		// 3) "node2"
		scanList(redisTemplate, KEY1, 0, 4);
		System.out.println("---------------------------");


		// 127.0.0.1:6379> LPUSH list2 data1 data2 data3
		// (integer) 3

		List<String> list2 = new ArrayList<String>();
		for (int i = 3; i >= 1; i--) {
			list2.add("data" + i);
		}
		System.out.println(redisTemplate.opsForList().leftPushAll(KEY2, list2));
		System.out.println("---------------------------");


		// 127.0.0.1:6379> RPOPLPUSH list list2
		// "node2"
		// 相当于 rpoplpush 命令，弹出 list1最右边的节点，插入到 list2 最左边
		String value2 = (String) redisTemplate.opsForList().rightPopAndLeftPush(KEY1, KEY2);
		System.out.println("rightPopAndLeftPush:" + value2);
		System.out.println("-------------------");

		// 127.0.0.1:6379> BRPOPLPUSH list list2 3
		// "node3"
		// 相当于 brpoplpush 命令，注意在 Spring 中使用超时参数区分
		String value3 = (String) redisTemplate.opsForList().rightPopAndLeftPush(KEY1, KEY2, 3, TimeUnit.SECONDS);
		System.out.println("rightPopAndLeftPush:" + value3);
		System.out.println("-------------------");

		// 127.0.0.1:6379> LRANGE list 0 10
		// 1) "node4"
		scanList(redisTemplate, KEY1, 0, 10);
		System.out.println("-------------------");

		// 127.0.0.1:6379> LRANGE list2 0 10
		// 1) "node3"
		// 2) "node2"
		// 3) "data3"
		// 4) "data2"
		// 5) "data1"
		scanList(redisTemplate, KEY2, 0, 10);
		
	}

	private static void scanList(RedisTemplate redisTemplate, String key, int begin, int end) {
		List<String> data = redisTemplate.opsForList().range(key, begin, end);
		for (String string : data) {
			System.out.println("节点：" + string);
		}
	}
}
