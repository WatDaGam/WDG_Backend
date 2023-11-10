package com.wdg.wdgbackend.controller.service;

import com.wdg.wdgbackend.model.repository.LikeRepository;
import com.wdg.wdgbackend.model.repository.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void linkUserAndStory(Long userId, Long storyId) {
        likeRepository.linkUserAndStory(userId, storyId);
    }

    public boolean isLiked(Long userId, Long storyId) {
        return likeRepository.isLiked(userId, storyId);
    }

    public void deleteStoryLikes(Long storyId) {
        likeRepository.deleteStoryLikes(storyId);
    }

    public void deleteUserLikes(Long userId) {
        likeRepository.deleteUserLikes(userId);
    }

    public void lockStoryLike(Long storyId) {
        storyRepository.lockStory(storyId);
    }

    public void likePlus(Long storyId) {
        storyRepository.likePlus(storyId);
    }

}
