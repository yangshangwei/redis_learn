package com.artisan.redis.publish;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisMessageListener2 implements MessageListener {

	private RedisTemplate redisTemplate;

	public RedisTemplate getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void onMessage(Message message, byte[] bytes) {
		// 获取消息
		byte[] body = message.getBody();
		// 使用值序列化器转换
		String msgBody = (String) getRedisTemplate().getValueSerializer().deserialize(body);
		System.out.println("RedisMessageListener2:" + msgBody);
		// 获取 channel
		byte[] channel = message.getChannel();
		// 使用字符串序列化器转换
		String channelStr = (String) getRedisTemplate().getStringSerializer().deserialize(channel);
		System.out.println("RedisMessageListener2:" + channelStr);
		// 渠道名称转换
		String bytesStr = new String(bytes);
		System.out.println("RedisMessageListener2:" + bytesStr);
	}

}
