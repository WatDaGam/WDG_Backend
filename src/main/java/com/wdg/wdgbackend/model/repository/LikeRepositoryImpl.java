package com.wdg.wdgbackend.model.repository;

import com.wdg.wdgbackend.model.mapper.LikeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LikeRepositoryImpl implements LikeRepository {

    private final LikeMapper likeMapper;

    @Autowired
    public LikeRepositoryImpl(LikeMapper likeMapper) {
        this.likeMapper = likeMapper;
    }

    @Override
    public void linkUserAndStory(long userId, long storyId, long writerId) {
        likeMapper.insertLike(userId, storyId, writerId);
    }

    @Override
    public boolean isLiked(long userId, long storyId) {
        return likeMapper.isLiked(userId, storyId);
    }

    @Override
    public void deleteStoryLikes(long storyId) {
        likeMapper.deleteStoryLikes(storyId);
    }

    @Override
    public void deleteUserLikes(long userId) {
        likeMapper.deleteUserLikes(userId);
    }
}
