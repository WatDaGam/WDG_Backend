package com.wdg.wdgbackend.model.entity;

import java.sql.Timestamp;

public class User {

	private Long id;
	private int snsId;
	private String nickname;
	private SNSPlatform sns;

	public User(Long id, int snsId, SNSPlatform sns) {
		this.id = id;
		this.snsId = snsId;
		this.sns = sns;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getSnsId() {
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
