package com.wdg.wdgbackend.controller.service;

import com.wdg.wdgbackend.controller.util.CustomException;
import com.wdg.wdgbackend.model.entity.User;
import com.wdg.wdgbackend.model.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
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
		try {
			long userId = tokenServcie.getIdFromAccessToken(authorizationHeader);
			User user = userRepository.findUserById(userId);

			if (user == null) {
				throw new CustomException("Invalid user", new IllegalArgumentException(), HttpStatus.NOT_FOUND);
			}

			JSONObject userJson = new JSONObject();
			userJson.put("nickname", user.getNickname());
			userJson.put("storyNum", user.getStoryNum());
			userJson.put("likeNum", user.getLikeNum());
			userJson.put("createdAt", user.getCreatedAt());
			userJson.put("reportedStoryNum", user.getReportedStoryNum());
			userRepository.clearReportedStoryNum(userId);

			return userJson.toString();
		} catch (DataAccessException e) {
			log.error("Database 에러 발생", e);
			throw new CustomException("Database error", e, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error("기다 에러 발생", e);
			throw new CustomException(e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public void incrementStoryNum(long userId) throws DataAccessException {
		userRepository.incrementStoryNum(userId);
	}

	public void decrementStoryNum(long userId) throws DataAccessException {
		userRepository.decrementStoryNum(userId);
	}

	public void lockUserLikeNum(long writerId) throws DataAccessException {
		userRepository.lockUserLikeNum(writerId);
	}

	public void incrementLikeNum(long writerId) throws DataAccessException {
		userRepository.incrementLikeNum(writerId);
	}
}
