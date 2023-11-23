package com.wdg.wdgbackend.model.entity;

import java.sql.Timestamp;

public class Story {

	private Long id;
	private Long userId;
	private String nickname;
	private String content;
	private Integer likeNum;
	private Long createdAt;
	private Double lati;
	private Double longi;

	public Story(Long id, Long userId, String nickname, String content, Integer likeNum, Double lati, Double longi, Long createdAt) {
		this.id = id;
		this.userId = userId;
		this.nickname = nickname;
		this.content = content;
		this.likeNum = likeNum;
		this.lati = lati;
		this.longi = longi;
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
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

	public Integer getLikeNum() {
		return likeNum;
	}

	public void setLikeNum(Integer likeNum) {
		this.likeNum = likeNum;
	}

	public Double getLati() {
		return lati;
	}

	public void setLati(Double lati) {
		this.lati = lati;
	}

	public Double getLongi() {
		return longi;
	}

	public void setLongi(Double longi) {
		this.longi = longi;
	}

	public Long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}
}
