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

	/**
	 * 신고api반환값
	 * 200 Ok -> 신고 성공, 메세지 삭제 안됨
	 * 205 Reset Content -> 신고 성공, 메세지 삭제
	 * 208 Already Reported -> 중복된 신고
	 *
	 * @param authorizationHeader
	 * @param storyId
	 * @return
	 */
	@Transactional
	public int reportNumUpdate(String authorizationHeader, String storyId) {
		try {
			long userId = tokenService.getIdFromAccessToken(authorizationHeader);
			long storyIdL = Long.parseLong(storyId);

			Optional<Story> story = Optional.ofNullable(storyRepository.getStory(storyIdL));

			/*
			 * 이미 글이 신고로 인해 삭제되었을 경우 205 반환
			 */
			if (story.isEmpty()) return 205;

			/*
			 * 이미 전에 해당 유저가 글을 삭제했을 경우 208 반환
			 */
			if (reportRepository.isReported(userId, storyIdL)) return 208;

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
