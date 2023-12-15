package com.wdg.wdgbackend.model.repository;

public interface ReportedStoryRepository {
	void insertReportedStory(long userId, long storyId, String content);
}
