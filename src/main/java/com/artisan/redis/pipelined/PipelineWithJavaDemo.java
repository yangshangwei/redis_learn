package com.artisan.redis.pipelined;

import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

public class PipelineWithJavaDemo {

	public static void main(String[] args) {

		// 实例化jedisPoolConfig并设置相关参数
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(6);
		jedisPoolConfig.setMaxTotal(20);
		jedisPoolConfig.setMaxWaitMillis(3000);
		jedisPoolConfig.setMinEvictableIdleTimeMillis(300000);
		jedisPoolConfig.setTimeBetweenEvictionRunsMillis(30000);
		// 使用jedisPoolConfig初始化redis的连接池
		JedisPool jedisPool = new JedisPool(jedisPoolConfig, "192.168.31.66", 6379);
		// 从jedisPool中获取一个连接
		Jedis jedis = jedisPool.getResource();
		jedis.auth("artisan");

		long beginTime = System.currentTimeMillis();

		// 开启流水线
		Pipeline pipeline = jedis.pipelined();

		// 测试10 万条的读／写 2 个操作
		for (int i = 0; i < 100000; i++) {
			int j = i + 1 ;
			jedis.set("pipeline_key" + j , "pipeline_value" + j);
			jedis.get("pipeline_key" + j);
		}

		// 这里只执行同步，但是不返回结果
		// pipeline.sync();

		// 将返回执行过的命令返回的 List 列表结果
		List list = pipeline.syncAndReturnAll();

		long endTime = System.currentTimeMillis();
		System.out.println("10 万条的读／写 2 个操作 ,耗时：" + (endTime - beginTime) + "毫秒");
	}

}
