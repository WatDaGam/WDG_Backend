package com.wdg.wdgbackend.controller.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.wdg.wdgbackend.model.entity.Story;
import com.wdg.wdgbackend.model.repository.StoryRepository;
import com.wdg.wdgbackend.model.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StoryService {

	private final StoryRepository storyRepository;
	private final UserRepository userRepository;
	private final TokenService tokenService;

	@Autowired
	public StoryService(StoryRepository storyRepository, UserRepository userRepository, TokenService tokenService) {
		this.storyRepository = storyRepository;
		this.userRepository = userRepository;
		this.tokenService = tokenService;
	}

	@Transactional
	public void insertNewStory(String authorizationHeader, JsonNode rootNode) {
		Long userId = tokenService.getIdFromAccessToken(authorizationHeader);
		String nickname = tokenService.getNicknameFromAccessToken(authorizationHeader);
		String story = rootNode.get("content").asText();
		double lati = rootNode.get("lati").asDouble();
		double longi = rootNode.get("longi").asDouble();

		userRepository.incrementStoryNum(userId);
		storyRepository.insertStory(new Story(0L, userId, nickname, story, 0, lati, longi));
	}

	@Transactional
	public void likePlus(Long storyId) {
		storyRepository.lockStory(storyId);
		storyRepository.likePlus(storyId);
	}

	@Transactional
	public void likeMinus(Long storyId) {
		storyRepository.lockStory(storyId);
		storyRepository.likeMinus(storyId);
	}

	public String makeStoryJSONObject(Long storyId) {
		Story story = storyRepository.getStory(storyId);
		JSONObject storyJson = new JSONObject();

		storyJson.put("storyId", story.getId());
		storyJson.put("nickname", story.getNickname());
		storyJson.put("content", story.getContent());
		storyJson.put("likeNum", story.getLikeNum());

		return storyJson.toString();
	}

	@Transactional
	public void deleteStory(String authorizationHeader, String storyId) {
		Long userId = tokenService.getIdFromAccessToken(authorizationHeader);
		userRepository.decrementStoryNum(userId);
		storyRepository.deleteStory(Long.parseLong(storyId));
	}
}
