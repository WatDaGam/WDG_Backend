package com.wdg.wdgbackend.model.repository;

public interface LikeRepository {
    void linkUserAndStory(Long userId, Long storyId);
    boolean isLiked(Long userId, Long storyId);

    void deleteStoryLikes(Long storyId);
    void deleteUserLikes(Long userId);
}
