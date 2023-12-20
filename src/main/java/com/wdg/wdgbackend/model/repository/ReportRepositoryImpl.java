package com.wdg.wdgbackend.model.repository;

import com.wdg.wdgbackend.model.mapper.ReportMapper;
import com.wdg.wdgbackend.model.mapper.StoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ReportRepositoryImpl implements ReportRepository {

	private final ReportMapper reportMapper;
	private final StoryMapper storyMapper;

	@Autowired
	public ReportRepositoryImpl(ReportMapper reportMapper, StoryMapper storyMapper) {
		this.reportMapper = reportMapper;
		this.storyMapper = storyMapper;
	}

	@Override
	public void reportStory(long userId, long storyId) {
		storyMapper.lockStoryReportNum(storyId);
		storyMapper.increaseReportNum(storyId);
		reportMapper.insertReport(userId, storyId);
	}

	@Override
	public boolean isReported(long userId, long storyId) {
		return reportMapper.isReported(userId, storyId);
	}

	@Override
	public Integer getReportNum(long storyId) {
		return reportMapper.getReportNum(storyId);
	}

	@Override
	public void deleteReports(long storyId) {
		reportMapper.deleteReports(storyId);
	}

	@Override
	public void deleteUserReports(long userId) {
		reportMapper.deleteUserReports(userId);
	}
}
