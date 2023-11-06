package com.wdg.wdgbackend.model.entity;

import java.sql.Timestamp;

public class Story {

	private Long id;
	private Long userId;
	private String content;
	private Integer likeNum;
	private Double lati;
	private Double longi;
	private Timestamp createdAt;

	public Story(Long id, Long userId, String content, Integer likeNum, Double lati, Double longi) {
		this.id = id;
		this.userId = userId;
		this.content = content;
		this.likeNum = likeNum;
		this.lati = lati;
		this.longi = longi;
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

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
}
