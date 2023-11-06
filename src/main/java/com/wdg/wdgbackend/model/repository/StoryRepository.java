package com.wdg.wdgbackend.model.repository;

import com.wdg.wdgbackend.model.entity.Story;

public interface StoryRepository {
	void insertStory(Story story);

	void likePlus(Long storyId);
	void likeMinus(Long storyId);
	Integer lockStory(Long storyId);

	Story getStory(Long storyId);
	void deleteStory(Long storyId);
}
