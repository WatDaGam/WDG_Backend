package com.wdg.wdgbackend.controller.service;

import com.wdg.wdgbackend.controller.util.CustomException;
import com.wdg.wdgbackend.model.repository.LikeRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class LikeService {

	private final UserInfoService userInfoService;
	private final TokenService tokenService;
	private final StoryUserLikeService storyUserLikeService;
	private final LikeRepository likeRepository;

	@Autowired
	public LikeService(UserInfoService userInfoService, TokenService tokenService, StoryUserLikeService storyUserLikeService, LikeRepository likeRepository) {
		this.userInfoService = userInfoService;
		this.tokenService = tokenService;
		this.storyUserLikeService = storyUserLikeService;
		this.likeRepository = likeRepository;
	}

	@Transactional
	public void likePlus(String authorizationHeader, String id) {
		try {
			processLikePlus(authorizationHeader, id);

		} catch (NumberFormatException e) {
			log.error("storyId 포맷 에러", e);
			throw new CustomException("Invalid story ID format" + e.getMessage(), e, HttpStatus.BAD_REQUEST);
		}  catch (DataAccessException e) {
			log.error("Database 에러", e);
			throw new CustomException("Database access error: " + e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error("기타 에러", e);
			throw new CustomException("General error: " + e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private void processLikePlus(String authorizationHeader, String id) {
		long userId = tokenService.getIdFromAccessToken(authorizationHeader);
		long storyId = Long.parseLong(id);

		Optional<Long> writerId = storyUserLikeService.getUserIdFromStory(storyId);
		if (writerId.isEmpty() || storyUserLikeService.isLiked(userId, storyId)) return;

		try {
			userInfoService.incrementLikeNum(writerId.get());
			likeRepository.linkUserAndStory(userId, storyId, writerId.get());
			storyUserLikeService.lockAndIncreaseLikeNum(storyId);
		} catch (DataAccessException e) {
			throw new CustomException("Database access error: " + e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public JSONObject getStoryLikeNumJSON(String storyId) {
		JSONObject responseJSON = new JSONObject();
		responseJSON.put("likeNum", storyUserLikeService.getStoryLikeNum(Long.parseLong(storyId)));
		return responseJSON;
	}
}
