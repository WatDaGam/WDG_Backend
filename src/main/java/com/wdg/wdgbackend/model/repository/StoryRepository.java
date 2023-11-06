package com.wdg.wdgbackend.model.repository;

import com.wdg.wdgbackend.model.entity.Story;

public interface StoryRepository {
	void insertStory(Story story);
	Story getStory(Long storyId);
	void deleteStory(Long storyId);
}
