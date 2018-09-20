package com.artisan.redis.baseStructure.hash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

public class SpringRedisHashDemo {

	public static void main(String[] args) {
		
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/spring-redis-hash.xml");
		
		
		RedisTemplate redisTemplate = (RedisTemplate) ctx.getBean("redisTemplate");
		
		// 127.0.0.1:6379> HMSET obj k1 value1 k2 value2 k3 value3
		// OK
		String key = "obj";
		Map<String, String> map = new HashMap<String, String>();
		map.put("k1", "value1");
		map.put("k2", "value2");
		map.put("k3", "value3");
		redisTemplate.opsForHash().putAll(key, map);

		// 127.0.0.1:6379> HSET obj k4 6
		// (integer) 1
		redisTemplate.opsForHash().put(key, "k4", String.valueOf(6));

		// 127.0.0.1:6379> HEXISTS obj k2
		// (integer) 1
		boolean exist = redisTemplate.opsForHash().hasKey(key, "k2");
		System.out.println(key + " 这个键中是否存在 k2这个field:" + exist);

		// 127.0.0.1:6379> HGETALL obj
		// 1) "k1"
		// 2) "value1"
		// 3) "k2"
		// 4) "value2"
		// 5) "k3"
		// 6) "value3"
		// 7) "k4"
		// 8) "6"
		Map<String,String> map2 = redisTemplate.opsForHash().entries(key);
		if (map2 != null) {
			scanMap(map2);
		}

		// 127.0.0.1:6379> HINCRBY obj k4 8
		// (integer) 14
		System.out.println(redisTemplate.opsForHash().increment(key, "k4", 8));
		
		
		// 127.0.0.1:6379> HINCRBYFLOAT obj k4 6.2
		// "20.2"
		System.out.println(redisTemplate.opsForHash().increment(key, "k4", 6.2));

		// 127.0.0.1:6379> HKEYS obj
		// 1) "k1"
		// 2) "k2"
		// 3) "k3"
		// 4) "k4"
		Set<String> set = redisTemplate.opsForHash().keys(key);
		for (String str : set) {
			System.out.println(str);
		}
		// 127.0.0.1:6379> HMGET obj k1 k2 k4
		// 1) "value1"
		// 2) "value2"
		// 3) "20.2"
		List<String> list = new ArrayList<String>();
		list.add("k1");
		list.add("k2");
		list.add("k4");
		List<String> list2 = redisTemplate.opsForHash().multiGet(key, list);
		scanList(list2);
		
		
		// 127.0.0.1:6379> HLEN obj
		// (integer) 4
		System.out.println(redisTemplate.opsForHash().size(key));
		
		// 127.0.0.1:6379> HSETNX obj k2 test
		// (integer) 0
		System.out.println(redisTemplate.opsForHash().putIfAbsent(key, "k2", "test"));
		
		// 127.0.0.1:6379> HSETNX obj k5 test
		// (integer) 1
		System.out.println(redisTemplate.opsForHash().putIfAbsent(key, "k5", "test"));

		// 127.0.0.1:6379> HGETALL obj
		// 1) "k1"
		// 2) "value1"
		// 3) "k2"
		// 4) "value2"
		// 5) "k3"
		// 6) "value3"
		// 7) "k4"
		// 8) "20.2"
		// 9) "k5"
		// 10) "test"

		Map<String, String> map3 = redisTemplate.opsForHash().entries(key);
		if (map3 != null) {
			scanMap(map3);
		}

		// 127.0.0.1:6379> HVALS obj
		// 1) "value1"
		// 2) "value2"
		// 3) "value3"
		// 4) "20.2"
		// 5) "test"

		List<String> list3 = redisTemplate.opsForHash().values(key);
		scanList(list3);

		// 127.0.0.1:6379> HDEL obj k5
		// (integer) 1
		redisTemplate.opsForHash().delete(key, "k5");

		// 127.0.0.1:6379> HGETALL obj
		// 1) "k1"
		// 2) "value1"
		// 3) "k2"
		// 4) "value2"
		// 5) "k3"
		// 6) "value3"
		// 7) "k4"
		// 8) "20.2"

		Map<String, String> map4 = redisTemplate.opsForHash().entries(key);
		if (map4 != null) {
			scanMap(map4);
		}

		// 127.0.0.1:6379> HGET obj k4
		// "20.2"
		System.out.println(redisTemplate.opsForHash().get(key, "k4"));
	}


	private static void scanList(List<String> list2) {
		for (String string : list2) {
			System.out.println(string);
		}
	}

	private static void scanMap(Map<String, String> map4) {
		for (Map.Entry<String, String> entry : map4.entrySet()) {
			System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		}
	}
}
