package com.wdg.wdgbackend.model.repository;

import com.wdg.wdgbackend.model.entity.Story;
import com.wdg.wdgbackend.model.mapper.StoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
	public void likePlus(long storyId) {
		storyMapper.likePlus(storyId);
	}

	@Override
	public void likeMinus(long storyId) {
		storyMapper.likeMinus(storyId);
	}

	@Override
	public Optional<Long> getUserIdFromStory(long storyId) {
		return Optional.of(storyMapper.getUserIdFromStory(storyId));
	}

	@Override
	public String getContent(long storyId) {
		return storyMapper.getContent(storyId);
	}

	@Override
	public int getStoryLikeNum(long storyId) {
		return storyMapper.getStoryLikeNum(storyId);
	}

	@Override
	public int lockStoryLikeNum(long storyId) {
		return storyMapper.lockStoryLikeNum(storyId);
	}

	@Override
	public Story getStory(long storyId) {
		return storyMapper.getStory(storyId);
	}

	@Override
	public List<Story> getStoryByUserId(long userId) {
		return storyMapper.getStoryByUserId(userId);
	}

	@Override
	public void deleteStory(long storyId) {
		storyMapper.delete(storyId);
	}
}
