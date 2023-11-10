package com.wdg.wdgbackend.controller.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.wdg.wdgbackend.model.entity.Story;
import com.wdg.wdgbackend.model.repository.StoryRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StoryService {

	private final StoryRepository storyRepository;
	private final UserInfoService userInfoService;
	private final TokenService tokenService;
	private final StoryLikeCommonService storyLikeCommonService;

	@Autowired
	public StoryService(StoryRepository storyRepository, UserInfoService userInfoService, TokenService tokenService, StoryLikeCommonService storyLikeCommonService) {
		this.storyRepository = storyRepository;
		this.userInfoService = userInfoService;
		this.tokenService = tokenService;
		this.storyLikeCommonService = storyLikeCommonService;
	}

	@Transactional
	public void insertNewStory(String authorizationHeader, JsonNode rootNode) {
		Long userId = tokenService.getIdFromAccessToken(authorizationHeader);
		String nickname = tokenService.getNicknameFromAccessToken(authorizationHeader);
		String story = rootNode.get("content").asText();
		double lati = rootNode.get("lati").asDouble();
		double longi = rootNode.get("longi").asDouble();

		userInfoService.incrementStoryNum(userId);
		storyRepository.insertStory(new Story(0L, userId, nickname, story, 0, lati, longi));
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
		Long storyIdL = Long.parseLong(storyId);

		userInfoService.decrementStoryNum(userId);
		storyLikeCommonService.deleteStoryLikes(storyIdL);
		storyRepository.deleteStory(storyIdL);
	}
}
