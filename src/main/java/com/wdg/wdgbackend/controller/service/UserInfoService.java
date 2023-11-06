package com.wdg.wdgbackend.controller.service;

import com.wdg.wdgbackend.model.entity.User;
import com.wdg.wdgbackend.model.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {

	private final UserRepository userRepository;

	@Autowired
	public UserInfoService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User getUser(Long id) {
		return userRepository.findUserById(id);
	}

	public JSONObject makeUserJSONObject(User user) {
		JSONObject userJson = new JSONObject();

		userJson.put("nickname", user.getNickname());
		userJson.put("storyNum", user.getStoryNum());
		userJson.put("likeNum", user.getLikeNum());
		return userJson;
	}
}
