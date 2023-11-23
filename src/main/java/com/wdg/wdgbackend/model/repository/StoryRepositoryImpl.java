package com.wdg.wdgbackend.model.repository;

import com.wdg.wdgbackend.model.entity.Story;
import com.wdg.wdgbackend.model.mapper.StoryMapper;
import com.wdg.wdgbackend.model.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class StoryRepositoryImpl implements StoryRepository{

	private final StoryMapper storyMapper;

	@Autowired
	public StoryRepositoryImpl(StoryMapper storyMapper) {
		this.storyMapper = storyMapper;
	}

	@Override
	public void insertStory(Story story) {
		storyMapper.insert(story);
	}

	@Override
	public void likePlus(Long storyId) {
		storyMapper.likePlus(storyId);
	}

	@Override
	public void likeMinus(Long storyId) {
		storyMapper.likeMinus(storyId);
	}

	@Override
	public Long getUserIdFromStory(Long storyId) {
		return storyMapper.getUserIdFromStory(storyId);
	}

	@Override
	public Integer lockStory(Long storyId) {
		return storyMapper.lockStory(storyId);
	}

	@Override
	public Story getStory(Long storyId) {
		return storyMapper.getStory(storyId);
	}

	@Override
	public List<Story> getStoryByUserId(Long userId) {
		return storyMapper.getStoryByUserId(userId);
	}

	@Override
	public void deleteStory(Long storyId) {
		storyMapper.delete(storyId);
	}
}
