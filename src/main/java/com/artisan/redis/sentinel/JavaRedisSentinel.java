package com.artisan.redis.sentinel;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

public class JavaRedisSentinel {

	public static void main(String[] args) {
		// 连接池配置
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(50);
		jedisPoolConfig.setMaxTotal(100);
		jedisPoolConfig.setMaxWaitMillis(2000);

		// 哨兵信息
		Set<String> sentinelsSet = new HashSet<String>(Arrays.asList("192.168.31.66:26379", "192.168.31.56:26379", "192.168.31.176:26379"));

		// 创建连接池
		JedisSentinelPool sentinelPool = new JedisSentinelPool("mymaster", sentinelsSet, jedisPoolConfig, "artisan");

		// 获取客户端
		Jedis jedis = sentinelPool.getResource();

		// 执行指令测试下
		jedis.set("artisan_key", "artisan_value");
		String result = jedis.get("artisan_key");
		System.out.println("artisan_key对应的value为：" + result);

	}

}
