package com.artisan.redis.withSpring;

import java.io.Serializable;

/**
 * 
 * 
 * @ClassName: Artisan
 * 
 * @Description: 可序列化的对象，需要实现Serializable接口
 * 
 * @author: Mr.Yang
 * 
 * @date: 2018年9月17日 下午4:23:31
 */
public class Artisan implements Serializable {

	private static final long serialVersionUID = 7877839815306073009L;

	private long id;
	private String name;
	private String comments;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}
