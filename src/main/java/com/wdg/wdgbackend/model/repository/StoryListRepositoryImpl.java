package com.wdg.wdgbackend.model.repository;

import com.wdg.wdgbackend.model.entity.Story;
import com.wdg.wdgbackend.model.mapper.StoryListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class StoryListRepositoryImpl implements StoryListRepository {

	private final StoryListMapper storyListMapper;

	@Autowired
	public StoryListRepositoryImpl(StoryListMapper storyListMapper) {
		this.storyListMapper = storyListMapper;
	}

	@Override
	public List<Story> getStoriesByDistance(long userId, double lati, double longi, int initialLimit, int finalLimit) {
		return storyListMapper.selectNearestStories(userId, lati, longi, initialLimit, finalLimit);
	}

	@Override
	public void increaseRenewNum(long userId) {
		storyListMapper.incrementStoryNum(userId);
	}
}
