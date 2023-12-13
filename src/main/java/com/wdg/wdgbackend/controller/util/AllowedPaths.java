package com.wdg.wdgbackend.controller.util;

public enum AllowedPaths {
	LOGIN("/login"),
	MY_STORY("/myStory"),
	NICK_CHECK("/nickname/check"),
	NICK_SET("/nickname/set"),
	REPORT("/report"),
	STORY_UPLOAD("/story/upload"),
	STORY_INFO("/story/info"),
	LIKE_PLUS("/like/plus"),
	STORY_DELETE("/story/delete"),
	LIST_RENEW("/storyList/renew"),
	REFRESH_TOKEN("/refreshtoken"),
	USER_INFO("/userInfo"),
	WITHDRAWAL("/withdrawal");

	private final String path;

	AllowedPaths(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public static boolean isAllowed(String path) {
		for (AllowedPaths allowedPath : values()) {
			if (allowedPath.getPath().equals(path)) {
				return true;
			}
		}
		return false;
	}
}
