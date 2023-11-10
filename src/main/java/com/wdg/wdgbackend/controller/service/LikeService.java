package com.wdg.wdgbackend.controller.service;

import com.wdg.wdgbackend.controller.util.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class LikeService {

    private final UserInfoService userInfoService;
    private final TokenService tokenService;
    private final StoryLikeCommonService storyLikeCommonService;

    @Autowired
    public LikeService(UserInfoService userInfoService, TokenService tokenService, StoryLikeCommonService storyLikeCommonService) {
        this.userInfoService = userInfoService;
        this.tokenService = tokenService;
        this.storyLikeCommonService = storyLikeCommonService;
    }

    @Transactional
    public void likePlus(String authorizationHeader, String id) {
        try {
            Long userId = tokenService.getIdFromAccessToken(authorizationHeader);
            Long storyId = Long.parseLong(id);

            if (storyLikeCommonService.isLiked(userId, storyId)) return;

            storyLikeCommonService.linkUserAndStory(userId, storyId);
            userInfoService.lockUserLikeNum(userId);
            userInfoService.incrementLikeNum(userId);
            storyLikeCommonService.lockStoryLike(storyId);
            storyLikeCommonService.likePlus(storyId);
        } catch (NumberFormatException e) {
            log.error("storyId 포맷 에러", e);
            throw new CustomException("Invalid story ID format", e, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Database 에러", e);
            throw new CustomException("Error occurred while processing like", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
