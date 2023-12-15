package com.wdg.wdgbackend.model.repository;

import com.wdg.wdgbackend.model.mapper.ReportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ReportRepositoryImpl implements ReportRepository {

	private final ReportMapper reportMapper;

	@Autowired
	public ReportRepositoryImpl(ReportMapper reportMapper) {
		this.reportMapper = reportMapper;
	}

	@Override
	public void reportStory(long userId, long storyId) {
		reportMapper.reportStory(storyId);
//		reportMapper.insertReport(userId, storyId);

		if (!reportMapper.isReported(userId, storyId))
			reportMapper.insertReport(userId, storyId);
	}

	@Override
	public void addReportedStoryToUser(String newStory, long userId) {
		reportMapper.addReportedStoryToUser(newStory, userId);
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
	public Integer lockStoryReportNum(long storyId) {
		return reportMapper.lockStoryReportNum(storyId);
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
