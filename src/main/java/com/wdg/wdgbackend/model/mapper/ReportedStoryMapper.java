package com.wdg.wdgbackend.model.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReportedStoryMapper {

	@Insert("INSERT INTO reportedstory (userId, storyId, content) VALUES (#{userId}, #{storyId}, #{content})")
	void insertReportedStory(long userId, long storyId, String content);
}
