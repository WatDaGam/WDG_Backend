package com.wdg.wdgbackend.controller.service;

import com.wdg.wdgbackend.controller.util.CustomException;
import com.wdg.wdgbackend.model.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WithdrawalService {

	private final UserRepository userRepository;
	private final StoryLikeCommonService storyLikeCommonService;

	@Autowired
	public WithdrawalService(UserRepository userRepository, StoryLikeCommonService storyLikeCommonService) {
		this.userRepository = userRepository;
		this.storyLikeCommonService = storyLikeCommonService;
	}

	public void deleteUser(Long userId) {
		try {
			storyLikeCommonService.deleteUserLikes(userId);
			userRepository.deleteUserById(userId);
		} catch (DataAccessException e) {
			log.error("삭제 과정 중 Database 에러 발생", e);
			throw new CustomException("Database error", e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
