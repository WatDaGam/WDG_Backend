package com.wdg.wdgbackend.model.entity;

import java.sql.Timestamp;

public class User {

	private long id;
	private long snsId;
	private String nickname;
	private SNSPlatform sns;

	public User(long id, long snsId, SNSPlatform sns) {
		this.id = id;
		this.snsId = snsId;
		this.sns = sns;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSnsId() {
		return snsId;
	}

	public void setSnsId(int snsId) {
		this.snsId = snsId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public SNSPlatform getSns() {
		return sns;
	}

	public void setSns(SNSPlatform sns) {
		this.sns = sns;
	}
}
