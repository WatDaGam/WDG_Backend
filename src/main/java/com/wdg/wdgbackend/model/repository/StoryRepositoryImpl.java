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
	public void likePlus(Long storyId) {
		storyMapper.likePlus(storyId);
	}

	@Override
	public void likeMinus(Long storyId) {
		storyMapper.likeMinus(storyId);
	}

	@Override
	public Optional<Long> getUserIdFromStory(Long storyId) {
		return Optional.ofNullable(storyMapper.getUserIdFromStory(storyId));
	}

	@Override
	public String getContent(Long storyId) {
		return storyMapper.getContent(storyId);
	}

	@Override
	public Integer getStoryLikeNum(Long storyId) {
		return storyMapper.getStoryLikeNum(storyId);
	}

	@Override
	public Integer lockStoryLikeNum(Long storyId) {
		return storyMapper.lockStoryLikeNum(storyId);
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
