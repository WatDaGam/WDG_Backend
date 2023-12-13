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
	public void reportStory(Long userId, Long storyId) {
		reportMapper.reportStory(storyId);
//		reportMapper.insertReport(userId, storyId);

		if (!reportMapper.isReported(userId, storyId))
			reportMapper.insertReport(userId, storyId);
	}

	@Override
	public void addReportedStoryToUser(String newStory, Long userId) {
		reportMapper.addReportedStoryToUser(newStory, userId);
	}

	@Override
	public boolean isReported(Long userId, Long storyId) {
		return reportMapper.isReported(userId, storyId);
	}

	@Override
	public Integer getReportNum(Long storyId) {
		return reportMapper.getReportNum(storyId);
	}

	@Override
	public Integer lockStoryReportNum(Long storyId) {
		return reportMapper.lockStoryReportNum(storyId);
	}

	@Override
	public void deleteReports(Long storyId) {
		reportMapper.deleteReports(storyId);
	}

	@Override
	public void deleteUserReports(Long userId) {
		reportMapper.deleteUserReports(userId);
	}
}
