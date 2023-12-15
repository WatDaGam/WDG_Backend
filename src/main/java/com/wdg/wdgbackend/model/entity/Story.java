package com.wdg.wdgbackend.model.entity;

public class Story {

	private long id;
	private long userId;
	private String nickname;
	private String content;
	private int likeNum;
	private long createdAt;
	private double lati;
	private double longi;
	private int reportNum;

	public Story(long id, long userId, String nickname, String content, int likeNum, double lati, double longi, long createdAt) {
		this.id = id;
		this.userId = userId;
		this.nickname = nickname;
		this.content = content;
		this.likeNum = likeNum;
		this.lati = lati;
		this.longi = longi;
		this.createdAt = createdAt;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getLikeNum() {
		return likeNum;
	}

	public void setLikeNum(int likeNum) {
		this.likeNum = likeNum;
	}

	public double getLati() {
		return lati;
	}

	public void setLati(double lati) {
		this.lati = lati;
	}

	public double getlongi() {
		return longi;
	}

	public void setlongi(double longi) {
		this.longi = longi;
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	public int getReportNum() {
		return reportNum;
	}

	public void setReportNum(int reportNum) {
		this.reportNum = reportNum;
	}
}
