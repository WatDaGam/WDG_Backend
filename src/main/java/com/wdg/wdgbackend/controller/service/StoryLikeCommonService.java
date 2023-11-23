package com.wdg.wdgbackend.controller.service;

import com.wdg.wdgbackend.model.repository.LikeRepository;
import com.wdg.wdgbackend.model.repository.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class StoryLikeCommonService {

    private final StoryRepository storyRepository;
    private final LikeRepository likeRepository;

    @Autowired
    public StoryLikeCommonService(StoryRepository storyRepository, LikeRepository likeRepository) {
        this.storyRepository = storyRepository;
        this.likeRepository = likeRepository;
    }

    public void linkUserAndStory(Long userId, Long storyId, Long writerId) throws DataAccessException {
        likeRepository.linkUserAndStory(userId, storyId, writerId);
    }

    public boolean isLiked(Long userId, Long storyId) throws DataAccessException {
        return likeRepository.isLiked(userId, storyId);
    }

    public void deleteStoryLikes(Long storyId) throws DataAccessException {
        likeRepository.deleteStoryLikes(storyId);
    }

    public void deleteUserLikes(Long userId) throws DataAccessException {
        likeRepository.deleteUserLikes(userId);
    }

    public void lockStoryLike(Long storyId) throws DataAccessException {
        storyRepository.lockStory(storyId);
    }

    public void likePlus(Long storyId) throws DataAccessException {
        storyRepository.likePlus(storyId);
    }

    public Long getUserIdFromStory(Long storyId) throws DataAccessException {
        return storyRepository.getUserIdFromStory(storyId);
    }

}
