package com.wdg.wdgbackend.controller.service;

import com.wdg.wdgbackend.controller.util.CustomException;
import com.wdg.wdgbackend.model.repository.LikeRepository;
import com.wdg.wdgbackend.model.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final StoryLikeCommonService storyLikeCommonService;
    private final LikeRepository likeRepository;

    @Autowired
    public LikeService(UserInfoService userInfoService, UserRepository userRepository, TokenService tokenService, StoryLikeCommonService storyLikeCommonService, LikeRepository likeRepository) {
        this.userInfoService = userInfoService;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.storyLikeCommonService = storyLikeCommonService;
        this.likeRepository = likeRepository;
    }

    @Transactional
    public void likePlus(String authorizationHeader, String id) {
        try {
            long userId = tokenService.getIdFromAccessToken(authorizationHeader);
            long storyId = Long.parseLong(id);

            Optional<Long> writerId = storyLikeCommonService.getUserIdFromStory(storyId);

            if (storyLikeCommonService.isLiked(userId, storyId)) return;
            if (writerId.isPresent() && userRepository.isUserExists(writerId.get())) {
                long realWriter = writerId.get();
                userInfoService.lockUserLikeNum(realWriter);
                userInfoService.incrementLikeNum(realWriter);
                likeRepository.linkUserAndStory(userId, storyId, writerId.get());
            } else {
                likeRepository.linkUserAndStory(userId, storyId);
            }
            storyLikeCommonService.lockStoryLike(storyId);
            storyLikeCommonService.likePlus(storyId);
        } catch (NumberFormatException e) {
            log.error("storyId 포맷 에러", e);
            throw new CustomException("Invalid story ID format", e, HttpStatus.BAD_REQUEST);
        }  catch (DataAccessException e) {
            log.error("Database 에러", e);
            throw new CustomException(e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("기타 에러", e);
            throw new CustomException(e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public JSONObject getStoryLikeNumJSON(String storyId) {
        JSONObject responseJSON = new JSONObject();
        responseJSON.put("likeNum", getStoryLikeNum(Long.parseLong(storyId)));
        return responseJSON;
    }

    private Integer getStoryLikeNum(long storyId) {
        return storyLikeCommonService.getStoryLikeNum(storyId);
    }
}
