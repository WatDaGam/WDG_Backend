package com.wdg.wdgbackend.model.repository;

public interface LikeRepository {
    void linkUserAndStory(long userId, long storyId);
    void linkUserAndStory(long userId, long storyId, long writerId);
    boolean isLiked(long userId, long storyId);

    void deleteStoryLikes(long storyId);
    void deleteUserLikes(long userId);
}
