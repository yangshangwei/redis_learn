package com.artisan.redis.withjava;

import redis.clients.jedis.Jedis;

public class JavaRedisInsert {

	public static void main(String[] args) {
		// Jedis jedis = new Jedis("localhost", 6379);
		Jedis jedis = new Jedis("192.168.31.66", 6379);
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
