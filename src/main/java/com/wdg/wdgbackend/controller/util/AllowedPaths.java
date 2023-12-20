package com.wdg.wdgbackend.controller.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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

	private static final Set<String> ALLOWED_PATHS_SET = new HashSet<>();

	static {
		Arrays.stream(AllowedPaths.values())
				.map(AllowedPaths::getPath)
				.forEach(ALLOWED_PATHS_SET::add);
	}

	public static boolean isAllowed(String path) {
		return ALLOWED_PATHS_SET.contains(path);
	}
}
