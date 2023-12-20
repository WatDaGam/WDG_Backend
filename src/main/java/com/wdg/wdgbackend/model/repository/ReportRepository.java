package com.wdg.wdgbackend.model.repository;


public interface ReportRepository {
	void reportStory(long userId, long storyId);
	boolean isReported(long userId, long storyId);
	Integer getReportNum(long storyId);
	void deleteReports(long storyId);
	void deleteUserReports(long userId);
}
