package com.wdg.wdgbackend.model.repository;

import com.wdg.wdgbackend.model.entity.Story;

import java.sql.Timestamp;
import java.util.List;

public interface StoryRepository {
	void insertStory(Story story);

	void likePlus(Long storyId);
	void likeMinus(Long storyId);
	Long getUserIdFromStory(Long storyId);
	Integer lockStory(Long storyId);

	Story getStory(Long storyId);
	List<Story> getStoryByUserId(Long userId);
	void deleteStory(Long storyId);
}
