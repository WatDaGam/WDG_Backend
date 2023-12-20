package com.wdg.wdgbackend.controller.service;

import com.wdg.wdgbackend.model.repository.LikeRepository;
import com.wdg.wdgbackend.model.repository.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StoryUserLikeService {

    private final StoryRepository storyRepository;
    private final LikeRepository likeRepository;

    @Autowired
    public StoryUserLikeService(StoryRepository storyRepository, LikeRepository likeRepository) {
        this.storyRepository = storyRepository;
        this.likeRepository = likeRepository;
    }

    public boolean isLiked(long userId, long storyId) throws DataAccessException {
        return likeRepository.isLiked(userId, storyId);
    }

    public void deleteStoryLikes(long storyId) throws DataAccessException {
        likeRepository.deleteStoryLikes(storyId);
    }

    public void deleteUserLikes(long userId) throws DataAccessException {
        likeRepository.deleteUserLikes(userId);
    }

    public void lockAndIncreaseLikeNum(long storyId) throws DataAccessException {
        storyRepository.lockStoryLikeNum(storyId);
        storyRepository.incrementStoryLikeNum(storyId);
    }

    public Optional<Long> getUserIdFromStory(long storyId) throws DataAccessException {
        return storyRepository.getUserIdFromStory(storyId);
    }

    public Integer getStoryLikeNum(long storyId) {
        return storyRepository.getStoryLikeNum(storyId);
    }
}
