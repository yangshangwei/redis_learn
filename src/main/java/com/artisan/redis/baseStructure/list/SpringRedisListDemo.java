package com.artisan.redis.baseStructure.list;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.connection.RedisListCommands;
import org.springframework.data.redis.core.RedisTemplate;

public class SpringRedisListDemo {
	
	private static final String KEY = "list";
	
	public static void main(String[] args) {
		
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/spring-redis-list.xml");
		RedisTemplate redisTemplate = (RedisTemplate) ctx.getBean("redisTemplate");
		
		// 不管存不存在，先根据key清理掉链表，方便测试
		Boolean flag = redisTemplate.delete(KEY);
		System.out.println((flag = true) ? "删除list成功" : "删除list失败");
		
		// 127.0.0.1:6379> LPUSH list node3 node2 node1
		// (integer) 3

		// 把 node3 插入链表 list
		System.out.println(redisTemplate.opsForList().leftPush(KEY, "node3"));

		// 相当于 lpush 把多个价值从左插入链表
		List<String> nodeList = new ArrayList<String>();
		for (int i = 2; i >= 1; i--) {
			nodeList.add("node" + i);
		}
		System.out.println(redisTemplate.opsForList().leftPushAll(KEY, nodeList));

		// 127.0.0.1:6379> RPUSH list node4
		// (integer) 4
		// 从右边插入一个节点
		System.out.println(redisTemplate.opsForList().rightPush(KEY, "node4"));

		// 127.0.0.1:6379> LINDEX list 0
		// "node1"
		// 获取下标为 0 的节点
		String node = (String) redisTemplate.opsForList().index(KEY, 0);
		System.out.println("第一个节点:" + node);

		// 127.0.0.1:6379> LLEN list
		// (integer) 4
		// 获取链表长度
		System.out.println(KEY + "中的总数为：" + redisTemplate.opsForList().size(KEY));

		// 127.0.0.1:6379> LPOP list
		// "node1"
		// 从左边弹出 一个节点
		String leftPopNode = (String) redisTemplate.opsForList().leftPop(KEY);
		System.out.println("leftPopNode:" + leftPopNode);

		// 127.0.0.1:6379> RPOP list
		// "node4"
		// 从右边弹出 一个节点
		String rightPopNode = (String) redisTemplate.opsForList().rightPop(KEY);
		System.out.println("rightPopNode:" + rightPopNode);

		// 注意，需要使用更为底层的命令才能操作 linsert 命令
		// 127.0.0.1:6379> LINSERT list before node2 before_node
		// (integer) 3
		// 使用 linsert 命令在node2 前插入一个节点
		try {
			Long long1 = redisTemplate.getConnectionFactory().getConnection()
					.lInsert(KEY.getBytes("utf-8"), RedisListCommands.Position.BEFORE, "node2".getBytes("utf-8"), "before_node".getBytes("utf-8"));
			System.out.println(long1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}


		// 127.0.0.1:6379> LINSERT list after node2 after_node
		// (integer) 4
		// 使用 linsert 命令在 node2 后插入一个节点
		try {
			Long long2 = redisTemplate.getConnectionFactory().getConnection()
					.lInsert(KEY.getBytes("utf-8"), RedisListCommands.Position.AFTER, "node2".getBytes("utf-8"), "after_node".getBytes("utf-8"));
			System.out.println(long2);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 127.0.0.1:6379> LPUSHX list head
		// (integer) 5
		// 判断 list 是否存在，如果存在则从左边插入 head 节点
		System.out.println(redisTemplate.opsForList().leftPushIfPresent(KEY, "head"));

		// 127.0.0.1:6379> RPUSHX list end
		// (integer) 6
		// 判断 list 是否存在，如果存在则从右边插入 end 节点
		System.out.println(redisTemplate.opsForList().rightPushIfPresent(KEY, "end"));

		// 127.0.0.1:6379> LRANGE list 0 10
		// 1) "head"
		// 2) "before_node"
		// 3) "node2"
		// 4) "after_node"
		// 5) "node3"
		// 6) "end"
		List<String> list = redisTemplate.opsForList().range(KEY, 0, 10);
		for (String string : list) {
			System.out.println("节点：" + string);
		}

		// 127.0.0.1:6379> LPUSH list node node node
		// (integer) 9
		// 在链表左边插入三个值为 node 的节点
		nodeList.clear();
		for (int i = 0; i < 3; i++) {
			nodeList.add("node");
		}
		System.out.println(redisTemplate.opsForList().leftPushAll(KEY, nodeList));

		// 127.0.0.1:6379> LREM list 3 node
		// (integer) 3
		// 从左到右删除至多三个 node 节点
		System.out.println(redisTemplate.opsForList().remove(KEY, 3, "node"));

		// 127.0.0.1:6379> LSET list 0 new_head_value
		// OK
		// 给链表下标为 0 的节点设置新值
		redisTemplate.opsForList().set(KEY, 0, "new_head_value");

		// 127.0.0.1:6379> LRANGE list 0 10
		// 1) "new_head_value"
		// 2) "before_node"
		// 3) "node2"
		// 4) "after_node"
		// 5) "node3"
		// 6) "end"
		list = redisTemplate.opsForList().range(KEY, 0, 10);
		for (String string : list) {
			System.out.println("节点：" + string);
		}

		// 127.0.0.1:6379> LTRIM list 0 2
		// OK
		redisTemplate.opsForList().trim(KEY, 0, 2);
		System.out.println("---------------------");
		// 127.0.0.1:6379> LRANGE list 0 10
		// 1) "new_head_value"
		// 2) "before_node"
		// 3) "node2"
		list = redisTemplate.opsForList().range(KEY, 0, 10);
		for (String string : list) {
			System.out.println("节点：" + string);
		}
	}
}
