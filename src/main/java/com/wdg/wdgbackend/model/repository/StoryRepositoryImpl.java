package com.wdg.wdgbackend.model.repository;

import com.wdg.wdgbackend.model.entity.Story;
import com.wdg.wdgbackend.model.mapper.StoryMapper;
import com.wdg.wdgbackend.model.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
	public void lockStory(Long storyId) {
		storyMapper.lockStory(storyId);
	}

	@Override
	public Story getStory(Long storyId) {
		return storyMapper.getStory(storyId);
	}

	@Override
	public void deleteStory(Long storyId) {
		storyMapper.delete(storyId);
	}


}
