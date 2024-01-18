package com.wdg.wdgbackend.model.entity;

public enum SNSPlatform {
	KAKAO,
	GOOGLE,
	APPLE;

    public boolean isValid() {
		return true;
    }
}
