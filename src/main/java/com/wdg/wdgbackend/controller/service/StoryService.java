package com.wdg.wdgbackend.controller.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.wdg.wdgbackend.controller.util.CustomException;
import com.wdg.wdgbackend.model.entity.Story;
import com.wdg.wdgbackend.model.repository.StoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
		try {
			Long userId = tokenService.getIdFromAccessToken(authorizationHeader);
			String nickname = tokenService.getNicknameFromAccessToken(authorizationHeader);

			String story = rootNode.get("content").asText();
			double lati = rootNode.get("lati").asDouble();
			double longi = rootNode.get("longi").asDouble();

			userInfoService.incrementStoryNum(userId);
			storyRepository.insertStory(new Story(0L, userId, nickname, story, 0, lati, longi));
		} catch (DataAccessException | IllegalArgumentException e) {
			log.error("story 추가 중 에러 발생", e);
			throw new CustomException("Error occurred while inserting a story", e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public String makeStoryJSONObject(String storyIdS) {
		long storyId = 0L;
		try {
			storyId = Long.parseLong(storyIdS);
		} catch (NumberFormatException e) {
			throw new CustomException("storyId Number format error", e, HttpStatus.BAD_REQUEST);
		}
		Story story = storyRepository.getStory(storyId);
		if (story == null) throw new CustomException("Invalid story", new IllegalArgumentException(), HttpStatus.NOT_FOUND);
		JSONObject storyJson = new JSONObject();

		storyJson.put("storyId", story.getId());
		storyJson.put("nickname", story.getNickname());
		storyJson.put("content", story.getContent());
		storyJson.put("likeNum", story.getLikeNum());

		return storyJson.toString();
	}

	@Transactional
	public void deleteStory(String authorizationHeader, String storyId) {
		try {
			Long userId = tokenService.getIdFromAccessToken(authorizationHeader);
			Long storyIdL = Long.parseLong(storyId);

			userInfoService.decrementStoryNum(userId);
			storyLikeCommonService.deleteStoryLikes(storyIdL);
			storyRepository.deleteStory(storyIdL);
		} catch (DataAccessException | IllegalArgumentException e) {
			log.error("story 삭제 중 에러 발생", e);
			throw new CustomException("Error occurred while deleting a story", e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
