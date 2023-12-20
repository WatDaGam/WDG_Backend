package com.wdg.wdgbackend.controller.service;

import com.wdg.wdgbackend.controller.util.CustomException;
import com.wdg.wdgbackend.model.entity.Story;
import com.wdg.wdgbackend.model.repository.StoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class MyStoryService {

	private final StoryRepository storyRepository;
	private final TokenService tokenService;

	@Autowired
	public MyStoryService(StoryRepository storyRepository, TokenService tokenService) {
		this.storyRepository = storyRepository;
		this.tokenService = tokenService;
	}

	@Transactional
	public JSONObject getStories(String authorizationHeader) {
		long userId = tokenService.getIdFromAccessToken(authorizationHeader);
		try {
			List<Story> userStories = storyRepository.getStoryByUserId(userId);
			JSONObject response = new JSONObject();
			response.put("stories", new JSONArray(userStories));
			return response;
		} catch (DataAccessException e) {
			log.error("Database 에러", e);
			throw new CustomException("Database error", e, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (JSONException e) {
			log.error("JSON 파싱 에러", e);
			throw new CustomException("JSON parsing error", e, HttpStatus.BAD_REQUEST);
		}

	}
}
