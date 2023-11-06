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
}
