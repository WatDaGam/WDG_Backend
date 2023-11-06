package com.wdg.wdgbackend.controller.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wdg.wdgbackend.model.entity.Story;
import com.wdg.wdgbackend.model.repository.StoryRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoryService {

	private final StoryRepository storyRepository;
	private final TokenService tokenService;

	@Autowired
	public StoryService(StoryRepository storyRepository, TokenService tokenService) {
		this.storyRepository = storyRepository;
		this.tokenService = tokenService;
	}

	public void insertNewStory(String authorizationHeader, JsonNode rootNode) {
		Long userId = tokenService.getIdFromAccessToken(authorizationHeader);
		String nickname = tokenService.getNicknameFromAccessToken(authorizationHeader);
		String story = rootNode.get("story").asText();
		double lati = rootNode.get("lati").asDouble();
		double longi = rootNode.get("longi").asDouble();

		storyRepository.insertStory(new Story(0L, userId, nickname, story, 0, lati, longi));
	}

	public String makeStoryJSONObject(Long id) {
		Story story = storyRepository.getStory(id);
		JSONObject storyJson = new JSONObject();

		storyJson.put("nickname", story.getNickname());
		storyJson.put("story", story.getContent());
		storyJson.put("likeNum", story.getLikeNum());

		return storyJson.toString();
	}
}
