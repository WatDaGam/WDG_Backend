package com.wdg.wdgbackend.model.entity;

import java.sql.Timestamp;

public class User {

	private Long id;
	private Long snsId;
	private Integer sns;
	private String nickname;
	private Timestamp createdAt;
	private Boolean isActive;

	public User(Long id, Long snsId, Integer sns) {
		this.id = id;
		this.snsId = snsId;
		this.sns = sns;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Boolean getActive() {
		return isActive;
	}

	public void setActive(Boolean active) {
		isActive = active;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSnsId() {
		return snsId;
	}

	public void setSnsId(Long snsId) {
		this.snsId = snsId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Integer getSns() {
		return sns;
	}

	public void setSns(Integer sns) {
		this.sns = sns;
	}
}
