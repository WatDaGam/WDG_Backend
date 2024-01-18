package com.wdg.wdgbackend.model.entity;

import java.util.List;

public class User {

	private long id;
	private String snsId;
	private int platform;
	private String nickname;
	private long createdAt;
	private int storyNum;
	private int likeNum;
	private int reportedStoryNum;
	private int renewNum;
	private boolean isActive;

	public User(long id, String snsId, int platform, long createdAt) {
		this.id = id;
		this.snsId = snsId;
		this.platform = platform;
		this.createdAt = createdAt;
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

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	public boolean getActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		isActive = active;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSnsId() {
		return snsId;
	}

	public void setSnsId(String snsId) {
		this.snsId = snsId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getPlatform() {
		return platform;
	}

	public void setPlatform(int platform) {
		this.platform = platform;
	}

	public int getReportedStoryNum() {
		return reportedStoryNum;
	}

	public void setReportedStoryNum(int reportedStoryNum) {
		this.reportedStoryNum = reportedStoryNum;
	}

	public int getRenewNum() {
		return renewNum;
	}

	public void setRenewNum(int renewNum) {
		this.renewNum = renewNum;
	}
}
