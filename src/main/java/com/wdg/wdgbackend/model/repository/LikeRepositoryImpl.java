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
    public void linkUserAndStory(Long userId, Long storyId) {
        likeMapper.insertLikeNULLUser(userId, storyId);
    }

    @Override
    public void linkUserAndStory(Long userId, Long storyId, Long writerId) {
        likeMapper.insertLike(userId, storyId, writerId);
    }

    @Override
    public boolean isLiked(Long userId, Long storyId) {
        return likeMapper.isLiked(userId, storyId);
    }

    @Override
    public void deleteStoryLikes(Long storyId) {
        likeMapper.deleteStoryLikes(storyId);
    }

    @Override
    public void deleteUserLikes(Long userId) {
        likeMapper.deleteUserLikes(userId);
    }
}
