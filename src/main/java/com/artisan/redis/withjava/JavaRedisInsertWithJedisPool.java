package com.artisan.redis.withjava;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JavaRedisInsertWithJedisPool {

	public static void main(String[] args) {
		// 实例化jedisPoolConfig并设置相关参数
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(50);
		jedisPoolConfig.setMaxTotal(100);
		jedisPoolConfig.setMaxWaitMillis(2000);
		// 使用jedisPoolConfig初始化redis的连接池
		JedisPool jedisPool = new JedisPool(jedisPoolConfig, "192.168.31.66", 6379);
		// 从jedisPool中获取一个连接
		Jedis jedis = jedisPool.getResource();
		jedis.auth("artisan");

		int i = 0 ;
		long beginTime = System.currentTimeMillis();
		try {
			while (true) {
				long endTime = System.currentTimeMillis();
				// 超过一秒 停止操作
				if (endTime - beginTime > 1000) {
					break;
				}
				i++;
				// 写入redis
				jedis.set("artisan-" + i, String.valueOf(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭redis
			jedis.close();
		}
		System.out.println("1S内对redis的写入次数为" + i);
	}

}
