package com.wdg.wdgbackend.controller.service;

import com.wdg.wdgbackend.controller.util.CustomException;
import com.wdg.wdgbackend.model.entity.Story;
import com.wdg.wdgbackend.model.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class ReportService {

	private static final int MAX_REPORT = 2;

	private final ReportRepository reportRepository;
	private final LikeRepository likeRepository;
	private final UserRepository userRepository;
	private final StoryRepository storyRepository;
	private final ReportedStoryRepository reportedStoryRepository;
	private final TokenService tokenService;
	private final StoryService storyService;

	@Autowired
	public ReportService(ReportRepository reportRepository, LikeRepository likeRepository, UserRepository userRepository, StoryRepository storyRepository, ReportedStoryRepository reportedStoryRepository, TokenService tokenService, StoryService storyService) {
		this.reportRepository = reportRepository;
		this.likeRepository = likeRepository;
		this.userRepository = userRepository;
		this.storyRepository = storyRepository;
		this.reportedStoryRepository = reportedStoryRepository;
		this.tokenService = tokenService;
		this.storyService = storyService;
	}

	@Transactional
	public int reportNumUpdate(String authorizationHeader, String storyId) {
		try {
			long userId = tokenService.getIdFromAccessToken(authorizationHeader);
			long storyIdL = Long.parseLong(storyId);

			Optional<Story> story = Optional.ofNullable(storyRepository.getStory(storyIdL));
			if (story.isEmpty()) return 205;

//			if (reportRepository.isReported(userId, storyIdL)) return;

			reportRepository.lockStoryReportNum(storyIdL);
			reportRepository.reportStory(userId, storyIdL);

			if (reportRepository.getReportNum(storyIdL) == MAX_REPORT) {
				Optional<Long> writerId = storyRepository.getUserIdFromStory(storyIdL);
				if (writerId.isPresent()) {
					reportedStoryRepository.insertReportedStory(userId, storyIdL, story.get().getContent());
					userRepository.incrementReportedStoryNum(userId);
				}
				storyService.deleteStoryConnectedNums(userId, storyIdL);
				likeRepository.deleteStoryLikes(storyIdL);
				reportRepository.deleteReports(storyIdL);
				storyRepository.deleteStory(storyIdL);

				return 205;
			}

		} catch (NumberFormatException e) {
			log.error("storyId 포맷 에러", e);
			throw new CustomException("Invalid story ID format", e, HttpStatus.BAD_REQUEST);
		} catch (DataAccessException e) {
			log.error("Database 에러", e);
			throw new CustomException(e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error("기타 에러", e);
			throw new CustomException(e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return 200;
	}
}
