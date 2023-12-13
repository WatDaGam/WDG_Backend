package com.wdg.wdgbackend.model.repository;


public interface ReportRepository {
	void reportStory(Long userId, Long storyId);
	void addReportedStoryToUser(String newStory, Long userId);
	boolean isReported(Long userId, Long storyId);
	Integer getReportNum(Long storyId);
	Integer lockStoryReportNum(Long storyId);
	void deleteReports(Long storyId);
	void deleteUserReports(Long userId);
}
