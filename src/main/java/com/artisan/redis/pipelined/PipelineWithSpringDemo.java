package com.artisan.redis.pipelined;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

public class PipelineWithSpringDemo {

	@SuppressWarnings({ "rawtypes", "unchecked", "unused", "resource" })
	public static void main(String[] args) {

		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/spring-redis-string.xml");
		RedisTemplate redisTemplate = ctx.getBean(RedisTemplate.class);
		
		SessionCallback sessionCallback = (SessionCallback) (RedisOperations ops) -> {
			
			for (int i = 0; i < 100000; i++) {
				int j = i + 1 ;
				ops.boundValueOps("pipeline_key" + j).set("pipeline_value" + j);
				ops.boundValueOps("pipeline_key" + j).get();
			}
			
			return null;
		};
		
		long beginTime = System.currentTimeMillis();
		// 执行 Redis 的流水线命令
		List list = redisTemplate.executePipelined(sessionCallback);

		long endTime = System.currentTimeMillis();
		System.out.println("10 万条的读／写 2 个操作 ,耗时：" + (endTime - beginTime));
	}

}
