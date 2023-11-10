package com.wdg.wdgbackend.controller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Long userId = tokenService.getIdFromAccessToken(authorizationHeader);
        Long storyId = Long.parseLong(id);

        if (storyLikeCommonService.isLiked(userId, storyId)) return;
        storyLikeCommonService.linkUserAndStory(userId, storyId);

        userInfoService.lockUserLikeNum(userId);
        userInfoService.incrementLikeNum(userId);
        storyLikeCommonService.lockStoryLike(storyId);
        storyLikeCommonService.likePlus(storyId);
    }


}
