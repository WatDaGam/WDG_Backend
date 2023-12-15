package com.wdg.wdgbackend.model.repository;

import com.wdg.wdgbackend.model.mapper.ReportedStoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ReportedStoryRepositoryImpl implements ReportedStoryRepository {

	private final ReportedStoryMapper reportedStoryMapper;

	@Autowired
	public ReportedStoryRepositoryImpl(ReportedStoryMapper reportedStoryMapper) {
		this.reportedStoryMapper = reportedStoryMapper;
	}


	@Override
	public void insertReportedStory(long userId, long storyId, String content) {
		reportedStoryMapper.insertReportedStory(userId, storyId, content);
	}
}
