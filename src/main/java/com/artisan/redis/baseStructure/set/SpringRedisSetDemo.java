package com.artisan.redis.baseStructure.set;

import java.util.List;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 
 * 
 * @ClassName: SpringRedisSetDemo
 * 
 * @Description: 记得将 RedisTemplate 值序列化器设置为 StringRedisSerializer 然后运行该代码
 * 
 * @author: Mr.Yang
 * 
 * @date: 2018年9月26日 下午3:22:57
 */
public class SpringRedisSetDemo {
	
	private static final String SET1 = "set1";
	private static final String SET2 = "set2";

	public static void main(String[] args) {
		
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/spring-redis-set.xml");
		RedisTemplate<String, String> redisTemplate = (RedisTemplate<String, String>) ctx.getBean("redisTemplate");

		Set<String> set = null;

		// 127.0.0.1:6379> FLUSHDB
		// OK
		// 127.0.0.1:6379> SADD set1 v1 v2 v3 v4 v5 v6
		// (integer) 6
		// 127.0.0.1:6379> SADD set2 v0 v2 v4 v6 v8
		// (integer) 5
		// 将元素加入列表
		redisTemplate.boundSetOps(SET1).add("v1", "v2", "v3", "v4", "v5", "v6");
		redisTemplate.boundSetOps(SET2).add("v0", "v2", "v4", "v6", "v8");
		
		// 127.0.0.1:6379> SCARD set1
		// (integer) 6
		// 求集合长度
		System.out.println(SET1 + "的长度为：" + redisTemplate.opsForSet().size(SET1));
		System.out.println(SET2 + "的长度为：" + redisTemplate.opsForSet().size(SET2));

		// 127.0.0.1:6379> SDIFF set1 set2
		// 1) "v5"
		// 2) "v1"
		// 3) "v3"
		// 求差集
		set = redisTemplate.opsForSet().difference(SET1, SET2);
		scanSet(set);

		// 求并集
		set = redisTemplate.opsForSet().intersect(SET1, SET2);
		scanSet(set);
		
		
		// 判断是否是集合中的元素
		boolean exist = redisTemplate.opsForSet().isMember(SET1, "v1");
		System.out.println(SET1 + "中存在v1:" + exist);

		// 获取集合所有元素
		set = redisTemplate.opsForSet().members(SET1);
		scanSet(set);
		set = redisTemplate.opsForSet().members(SET2);
		scanSet(set);

		// 从集合中随机弹出一个元素,集合中会删除该元素
		String randomValue = redisTemplate.opsForSet().pop(SET2);
		System.out.println(SET2 + "中弹出的随机元素为" + randomValue);
		System.out.println(SET2 + "的长度为：" + redisTemplate.opsForSet().size(SET2));
		
		// 随机获取一个集合的元素 ,该元素仍然在集合中
		randomValue = (String) redisTemplate.opsForSet().randomMember(SET1);
		System.out.println(randomValue);
		System.out.println(SET1 + "的长度为：" + redisTemplate.opsForSet().size(SET1));

		System.out.println("------------");
		
		// 随机获取集合中 的4 个元素
		List<String> list = redisTemplate.opsForSet().randomMembers(SET1, 4);
		for (String string : list) {
			System.out.println(string);
		}

		// 删除一个集合的元素，参数可以是多个
		Long value = redisTemplate.opsForSet().remove(SET1, "v1", "v2");
		System.out.println(SET1 + "中删除了" + value + "个元素");
		System.out.println(SET1 + "的长度为：" + redisTemplate.opsForSet().size(SET1));

		// 求两个集合的并集
		set = redisTemplate.opsForSet().union(SET1, SET2);
		scanSet(set);
		
		// 求两个集合的差集，并保存到集合 diff_set 中
		redisTemplate.opsForSet().differenceAndStore(SET1, SET2, "diff_set");
		scanSet(redisTemplate.opsForSet().members("diff_set"));

		// 求两个集合的交集，并保存到集合 inter_set 中
		redisTemplate.opsForSet().intersectAndStore(SET1, SET2, "inter_set");
		scanSet(redisTemplate.opsForSet().members("inter_set"));

		// 求两个集合的并集，并保存到集合 union_set 中
		redisTemplate.opsForSet().unionAndStore(SET1, SET2, "union_set");
		scanSet(redisTemplate.opsForSet().members("union_set"));

	}

	private static void scanSet(Set<String> set) {
		for (String value : set) {
			System.out.println(value);
		}
		System.out.println("----------------");
	}

}
