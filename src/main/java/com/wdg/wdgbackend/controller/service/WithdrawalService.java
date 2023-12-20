package com.wdg.wdgbackend.controller.service;

import com.wdg.wdgbackend.controller.util.CustomException;
import com.wdg.wdgbackend.model.repository.ReportRepository;
import com.wdg.wdgbackend.model.repository.StoryRepository;
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
	private final ReportRepository reportRepository;
	private final StoryUserLikeService storyUserLikeService;
	private final StoryRepository storyRepository;

	@Autowired
	public WithdrawalService(UserRepository userRepository, ReportRepository reportRepository, StoryUserLikeService storyUserLikeService, StoryRepository storyRepository) {
		this.userRepository = userRepository;
		this.reportRepository = reportRepository;
		this.storyUserLikeService = storyUserLikeService;
		this.storyRepository = storyRepository;
	}

	/**
	 * userId를 인자로 받으면, 해당 유저의 likes, reports, story를 삭제 후에 user를 삭제한다.
	 * @param userId
	 */
	public void deleteUser(Long userId) {
		try {
			storyUserLikeService.deleteUserLikes(userId);
			reportRepository.deleteUserReports(userId);
			storyRepository.deleteStoryWithUserId(userId);
			userRepository.deleteUserById(userId);
		} catch (DataAccessException e) {
			log.error("삭제 과정 중 Database 에러 발생", e);
			throw new CustomException("Database error", e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
