package com.wdg.wdgbackend.model.entity;

import java.sql.Timestamp;

public class User {

	private int snsId;
	private String nickname;
	private SNSPlatform sns;
	private Timestamp createdAt;

	public User(int snsId, String nickname, SNSPlatform sns, Timestamp createdAt) {
		this.snsId = snsId;
		this.nickname = nickname;
		this.sns = sns;
		this.createdAt = createdAt;
	}

	public int getSnsID() {
		return snsId;
	}

	public void setSnsID(int snsID) {
		this.snsId = snsID;
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

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
}
