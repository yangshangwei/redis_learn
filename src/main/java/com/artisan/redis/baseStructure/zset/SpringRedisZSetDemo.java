package com.artisan.redis.baseStructure.zset;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.connection.RedisZSetCommands.Limit;
import org.springframework.data.redis.connection.RedisZSetCommands.Range;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;


public class SpringRedisZSetDemo {

	private static final String ZSET1 = "zset1";
	private static final String ZSET2 = "zset2";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) {

		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/spring-redis-zset.xml");
		RedisTemplate redisTemplate = (RedisTemplate) ctx.getBean("redisTemplate");

		// 方便测试，清空数据
		redisTemplate.delete(ZSET1);
		redisTemplate.delete(ZSET2);

		// Spring 提供接口 TypedTuple 操作有序集合
		Set<TypedTuple> set1 = new HashSet<ZSetOperations.TypedTuple>();
		Set<TypedTuple> set2 = new HashSet<ZSetOperations.TypedTuple>();

		// 构造数据
		// 127.0.0.1:6379>
		// # zadd key score1 value1 [score2 value2 …] 向有序集合zset1 ，增加9个成员
		// >ZADD zset1 1 x1 2 x2 3 x3 4 x4 5 x5 6 x6 7 x7 8 x8 9 x9
		// (integer) 9

		// # zadd key score1 value1 [score2 value2 …] 向有序集合zset2 ，增加9个成员
		// > ZADD zset2 1 y1 2 x2 3 y3 4 x4 5 y5 6 x6 7 y7 8 x8 9 y9
		// (integer) 9
		int j = 9;
		
		String value1, value2 = null;
		double score1, score2 = 0.0;
		for (int i = 1; i <= 9; i++) {

			// 计算分数和值
			 score1 = Double.valueOf(i);
			 value1 = "x" + i;
			
			if (j > 0) {
				score2 = Double.valueOf(j);
				value2 = j % 2 == 1 ? "y" + j : "x" + j;
				j--;
			}
			// 使用 Spring提供的默认 TypedTuple-DefaultTypedTuple
			TypedTuple typedTuplel = new DefaultTypedTuple(value1,score1);
			set1.add(typedTuplel);
			TypedTuple typedTuple2 = new DefaultTypedTuple(value2,score2);
			set2.add(typedTuple2);

		}
		// 写入redis
		redisTemplate.opsForZSet().add(ZSET1, set1);
		redisTemplate.opsForZSet().add(ZSET2, set2);
		
		// 统计总数
		Long size = redisTemplate.opsForZSet().size(ZSET1);
		System.out.println(ZSET1 + "的size为" + size);

		// 计分数为 score ，那么下面的方法就是求 3<=score<=6 的元素
		Long count = redisTemplate.opsForZSet().count(ZSET1, 3, 6);
		System.out.println(ZSET1 + "中3<=score<=6 的count为" + count);

		// 从下标一开始截驭 5 个元素，但是不返回分数 ， 每一个元素是 String
		Set set = redisTemplate.opsForZSet().range(ZSET1, 1, 5);
		printSet(set);

		// 截取集合所有元素，并且对集合按分数排序，并返回分数 ， 每一个元素是 TypedTuple
		Set<TypedTuple> typedTuples = redisTemplate.opsForZSet().rangeWithScores(ZSET1, 0, -1);
		printTypedTuple(typedTuples);
		
		
		// 将 zsetl 和 zset2 两个集合的交集放入集合 inter_zset
		size = redisTemplate.opsForZSet().intersectAndStore(ZSET1, ZSET2, "inter_zset");
		System.out.println("inter_zset size:" + size);
		
		// 查看交集inter_zset中的数据
		set = redisTemplate.opsForZSet().range("inter_zset", 0, redisTemplate.opsForZSet().size("inter_zset"));
		printSet(set);

		// 区间
		Range range = Range.range();
		range.lt("x8");// 小于
		range.gt("x1");// 大于

		set = redisTemplate.opsForZSet().rangeByLex(ZSET1, range);
		printSet(set);

		range.lte("x8");// 小于等于
		range.gte("x1");// 大于等于

		set = redisTemplate.opsForZSet().rangeByLex(ZSET1, range);
		printSet(set);
		
		// 限制返回个数
		Limit limit = Limit.limit();
		// 限制返回个数
		limit.count(4);
		// 限制从第2个开始截取
		limit.offset(2);
		
		// 求区间内的元素，并限制返回 4 条
		set = redisTemplate.opsForZSet().rangeByLex(ZSET1, range, limit);
		printSet(set);

		// 求排行，排名第 1 返回 0 ，第 2 返回 1
		Long rank = redisTemplate.opsForZSet().rank(ZSET1, "x4");
		System.out.println("rank=" + rank);

		// 删除元素 ， 返回删除个数
		size = redisTemplate.opsForZSet().remove(ZSET1, "x5", "x6");
		System.out.println("remove " + size + " 个元素");

		// 按照排行删除从 0 开始算起，这里将删除第排名第 2 和第 3 的元素
		size = redisTemplate.opsForZSet().removeRange(ZSET1, 1, 2);
		System.out.println("removeRange " + size + " 个元素");

		// 获取所有集合的元索和分数 ， 以 -1 代表全部元素
		typedTuples = redisTemplate.opsForZSet().rangeWithScores(ZSET1, 0, -1);
		printTypedTuple(typedTuples);
		
		// 删除指定的元素
		size = redisTemplate.opsForZSet().remove(ZSET2, "y3", "y5");
		System.out.println("remove " + size + " 个元素");
		
		// 给集合中的一个元素的分数加上 11
		Double double1 = redisTemplate.opsForZSet().incrementScore(ZSET2, "y1", 11);
		printTypedTuple(redisTemplate.opsForZSet().rangeWithScores(ZSET2, 0, redisTemplate.opsForZSet().size(ZSET2)));
		
		// 从大到小排列
		typedTuples = redisTemplate.opsForZSet().reverseRangeWithScores(ZSET2, 0, 99);
		printTypedTuple(typedTuples);
	}
	
	@SuppressWarnings("rawtypes")
	public static void printTypedTuple(Set<TypedTuple> typedTuples) {
		if (typedTuples != null && typedTuples.isEmpty()) {
			return;
		}
		Iterator<TypedTuple> iterator = typedTuples.iterator();
		while (iterator.hasNext()) {
			TypedTuple typedTuple = iterator.next();
			System.out.println("{value =" + typedTuple.getValue() + ", score=" + typedTuple.getScore() + "}");
		}
		System.out.println("----------------------");
	}
	
	@SuppressWarnings("rawtypes")
	public static void printSet(Set set) {
		if (set != null && set.isEmpty()) {
			return;
		}
		Iterator iterator = set.iterator();
		while (iterator.hasNext()) {
			Object val = iterator.next();
			System.out.println(val + "\t");
		}
		System.out.println("----------------------");
	}
}