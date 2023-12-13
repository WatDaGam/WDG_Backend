package com.wdg.wdgbackend.model.repository;

import com.wdg.wdgbackend.model.entity.Story;

import java.util.List;
import java.util.Optional;

public interface StoryRepository {
	void insertStory(Story story);

	void likePlus(Long storyId);
	void likeMinus(Long storyId);
	Optional<Long> getUserIdFromStory(Long storyId);
	String getContent(Long storyId);
	Integer getStoryLikeNum(Long storyId);

	Integer lockStoryLikeNum(Long storyId);

	Story getStory(Long storyId);
	List<Story> getStoryByUserId(Long userId);
	void deleteStory(Long storyId);
}
