package com.wdg.wdgbackend.controller.service;

import com.wdg.wdgbackend.model.entity.User;
import com.wdg.wdgbackend.model.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {

	private final UserRepository userRepository;
	private final TokenService tokenServcie;

	@Autowired
	public UserInfoService(UserRepository userRepository, TokenService tokenServcie) {
		this.userRepository = userRepository;
		this.tokenServcie = tokenServcie;
	}

	public String makeUserJSONObject(String authorizationHeader) {
		Long userId = tokenServcie.getIdFromAccessToken(authorizationHeader);
		User user = userRepository.findUserById(userId);
		JSONObject userJson = new JSONObject();

		userJson.put("nickname", user.getNickname());
		userJson.put("storyNum", user.getStoryNum());
		userJson.put("likeNum", user.getLikeNum());

		return userJson.toString();
	}

	public void incrementStoryNum(Long userId) {
		userRepository.incrementStoryNum(userId);
	}

	public void decrementStoryNum(Long userId) {
		userRepository.decrementStoryNum(userId);
	}

	public void lockUserLikeNum(Long userId) {
		userRepository.lockUserLikeNum(userId);
	}

	public void incrementLikeNum(Long userId) {
		userRepository.incrementLikeNum(userId);
	}
}
