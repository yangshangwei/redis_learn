package com.artisan.redis.withSpring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

public class SpringRedisInsert {

	public static void main(String[] args) {
		
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/applicationContext.xml");
		
		RedisTemplate<String, Object> redisTemplate = (RedisTemplate) ctx.getBean("redisTemplate");

		Artisan artisan = new Artisan();
		artisan.setId(1L);
		artisan.setName("小工匠");
		artisan.setComments("使用Spring提供的RedisTemplate操作redis");
		// set
		redisTemplate.opsForValue().set("spring_redis_artisan", artisan);
		// get
		Artisan artisan1 = (Artisan) redisTemplate.opsForValue().get("spring_redis_artisan");
		System.out.println(artisan1.getId());
		System.out.println(artisan1.getName());
		System.out.println(artisan1.getComments());

		// 上面的set 和 get 方法看起来很简单 ， 它可 能就来自于 同 一个 Red is 连接池的不同 Redis 的连接。
		// 为了使得所有的操作都来自于同一个连接 ， 可 以使用 SessionCallback 或者 RedisCallback 这
		// 两个接口，而 RedisCallback 是 比较底层的封装 ， 其使用不是很友好，所以更多 的时候会使
		// 用 SessionCallback 这个接口 ， 通过这个接口就可以把多个命令放入到同一个 Redis 连接 中去执行
		Artisan artisan2 = new Artisan();
		artisan2.setId(2L);
		artisan2.setName("小工匠2");
		artisan2.setComments("2-使用Spring提供的RedisTemplate操作redis");

		SessionCallback<Artisan> sessionCallback = new SessionCallback<Artisan>() {
			@Override
			public Artisan execute(RedisOperations ops) throws DataAccessException {
				ops.boundValueOps("artisan_from_same_conn").set(artisan2);
				return (Artisan) ops.boundValueOps("artisan_from_same_conn").get();
			}
		};
		
		Artisan savedArtisan = redisTemplate.execute(sessionCallback);
		System.out.println(savedArtisan.getId());
		System.out.println(savedArtisan.getName());
		System.out.println(savedArtisan.getComments());
	}

}
