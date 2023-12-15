package com.wdg.wdgbackend.model.repository;

import com.wdg.wdgbackend.model.entity.Story;

import java.util.List;
import java.util.Optional;

public interface StoryRepository {
	void insertStory(Story story);

	void likePlus(long storyId);
	void likeMinus(long storyId);
	Optional<Long> getUserIdFromStory(long storyId);
	String getContent(long storyId);
	int getStoryLikeNum(long storyId);

	int lockStoryLikeNum(long storyId);

	Story getStory(long storyId);
	List<Story> getStoryByUserId(long userId);
	void deleteStory(long storyId);
}
