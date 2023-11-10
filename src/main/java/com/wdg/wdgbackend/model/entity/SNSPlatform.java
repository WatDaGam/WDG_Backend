package com.wdg.wdgbackend.model.entity;

public enum SNSPlatform {
	KAKAO,
	NAVER,
	GOOGLE;

    public boolean isValid() {
		return true;
    }
}
