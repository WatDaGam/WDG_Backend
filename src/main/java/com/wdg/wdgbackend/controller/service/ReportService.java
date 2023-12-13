package com.wdg.wdgbackend.controller.service;

import com.wdg.wdgbackend.controller.util.CustomException;
import com.wdg.wdgbackend.model.repository.LikeRepository;
import com.wdg.wdgbackend.model.repository.ReportRepository;
import com.wdg.wdgbackend.model.repository.StoryRepository;
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
	private final StoryRepository storyRepository;
	private final TokenService tokenService;

	@Autowired
	public ReportService(ReportRepository reportRepository, LikeRepository likeRepository, StoryRepository storyRepository, TokenService tokenService) {
		this.reportRepository = reportRepository;
		this.likeRepository = likeRepository;
		this.storyRepository = storyRepository;
		this.tokenService = tokenService;
	}

	@Transactional
	public void reportNumUpdate(String authorizationHeader, String storyId) {
		try {
			Long userId = tokenService.getIdFromAccessToken(authorizationHeader);
			Long storyIdL = Long.parseLong(storyId);

//			if (reportRepository.isReported(userId, storyIdL)) return;

			reportRepository.lockStoryReportNum(storyIdL);
			reportRepository.reportStory(userId, storyIdL);

			if (reportRepository.getReportNum(storyIdL) >= MAX_REPORT) {
				Optional<Long> writerId = storyRepository.getUserIdFromStory(storyIdL);
				if (writerId.isPresent()) {
					String content = storyRepository.getContent(storyIdL);
					reportRepository.addReportedStoryToUser(content, writerId.get());
				}
				likeRepository.deleteStoryLikes(storyIdL);
				reportRepository.deleteReports(storyIdL);
				storyRepository.deleteStory(storyIdL);
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
	}
}
