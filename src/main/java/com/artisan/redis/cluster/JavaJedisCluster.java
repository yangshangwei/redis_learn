package com.artisan.redis.cluster;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

public class JavaJedisCluster {

	public static void main(String[] args) {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		// 最大连接数
		poolConfig.setMaxTotal(1);
		// 最大空闲数
		poolConfig.setMaxIdle(1);
		// 最大允许等待时间
		poolConfig.setMaxWaitMillis(1000);
		
		// 集群地址
		Set<HostAndPort> nodes = new LinkedHashSet<HostAndPort>();
		nodes.add(new HostAndPort("192.168.31.66", 7000));
		nodes.add(new HostAndPort("192.168.31.66", 7001));
		nodes.add(new HostAndPort("192.168.31.56", 7002));
		nodes.add(new HostAndPort("192.168.31.56", 7003));
		nodes.add(new HostAndPort("192.168.31.176", 7004));
		nodes.add(new HostAndPort("192.168.31.176", 7005));
		
		// 实例化jedisCluster
		JedisCluster jedisCluster = new JedisCluster(nodes, poolConfig);
		
		// 搭建完成后手工set了一个key，这里直接获取
		String name = jedisCluster.get("mykey");
		System.out.println(name);
		
		// 通过api去set，然后get
		jedisCluster.set("mykey2", "code_redis_cluster");
		System.out.println(jedisCluster.get("mykey2"));
	
		try {
			// 关闭
			jedisCluster.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
