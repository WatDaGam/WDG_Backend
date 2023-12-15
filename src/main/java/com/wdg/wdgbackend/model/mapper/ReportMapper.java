package com.wdg.wdgbackend.model.mapper;

import org.apache.ibatis.annotations.*;

@Mapper
public interface ReportMapper {
	@Update("UPDATE story SET reportNum = reportNum + 1 WHERE id = #{storyId}")
	void reportStory(long storyId);

	@Update("UPDATE user \n" +
			"SET reportedStories = CASE \n" +
			"    WHEN reportedStories IS NULL THEN JSON_ARRAY(#{newStory}) \n" +
			"    ELSE JSON_ARRAY_APPEND(reportedStories, '$', #{newStory}) \n" +
			"END \n" +
			"WHERE id = #{userId}")
	void addReportedStoryToUser(String newStory, long userId);

	@Select("SELECT EXISTS (SELECT 1 FROM reports WHERE userId = #{userId} AND storyId = #{storyId})")
	boolean isReported(long userId, long storyId);

	@Insert("INSERT INTO reports (userId, storyId) VALUES (#{userId}, #{storyId})")
	void insertReport(long userId, long storyId);

	@Select("SELECT reportNum FROM story WHERE id = #{storyId}")
	int getReportNum(long storyId);

	@Select("SELECT reportNum FROM story WHERE id = #{storyId} FOR UPDATE")
	int lockStoryReportNum(long storyId);

	@Delete("DELETE FROM reports WHERE storyId = #{storyId}")
	void deleteReports(long storyId);

	@Delete("DELETE FROM reports WHERE userId = #{userId}")
	void deleteUserReports(long userId);
}
