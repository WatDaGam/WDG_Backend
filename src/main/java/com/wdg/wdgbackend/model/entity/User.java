package com.wdg.wdgbackend.model.entity;

import java.sql.Timestamp;

public class User {

	private Long id;
	private Long snsId;
	private Integer platform;
	private String nickname;
	private Timestamp createdAt;
	private int storyNum;
	private int likeNum;
	private Boolean isActive;

	public User(Long id, Long snsId, Integer platform) {
		this.id = id;
		this.snsId = snsId;
		this.platform = platform;
	}

	public int getStoryNum() {
		return storyNum;
	}

	public void setStoryNum(int storyNum) {
		this.storyNum = storyNum;
	}

	public int getLikeNum() {
		return likeNum;
	}

	public void setLikeNum(int likeNum) {
		this.likeNum = likeNum;
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

	public Integer getPlatform() {
		return platform;
	}

	public void setPlatform(Integer platform) {
		this.platform = platform;
	}
}
