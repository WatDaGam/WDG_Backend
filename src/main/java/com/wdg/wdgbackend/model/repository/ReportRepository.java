package com.wdg.wdgbackend.model.repository;


public interface ReportRepository {
	void reportStory(long userId, long storyId);
	void addReportedStoryToUser(String newStory, long userId);
	boolean isReported(long userId, long storyId);
	Integer getReportNum(long storyId);
	Integer lockStoryReportNum(long storyId);
	void deleteReports(long storyId);
	void deleteUserReports(long userId);
}
