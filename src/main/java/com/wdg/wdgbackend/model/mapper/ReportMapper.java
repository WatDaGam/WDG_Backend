package com.wdg.wdgbackend.model.mapper;

import org.apache.ibatis.annotations.*;

@Mapper
public interface ReportMapper {
	@Update("UPDATE story SET reportNum = reportNum + 1 WHERE id = #{storyId}")
	void reportStory(Long storyId);

	@Update("UPDATE user \n" +
			"SET reportedStories = CASE \n" +
			"    WHEN reportedStories IS NULL THEN JSON_ARRAY(#{newStory}) \n" +
			"    ELSE JSON_ARRAY_APPEND(reportedStories, '$', #{newStory}) \n" +
			"END \n" +
			"WHERE id = #{userId}")
	void addReportedStoryToUser(String newStory, Long userId);

	@Select("SELECT EXISTS (SELECT 1 FROM reports WHERE userId = #{userId} AND storyId = #{storyId})")
	boolean isReported(Long userId, Long storyId);

	@Insert("INSERT INTO reports (userId, storyId) VALUES (#{userId}, #{storyId})")
	void insertReport(Long userId, Long storyId);

	@Select("SELECT reportNum FROM story WHERE id = #{storyId}")
	Integer getReportNum(Long storyId);

	@Select("SELECT reportNum FROM story WHERE id = #{storyId} FOR UPDATE")
	Integer lockStoryReportNum(Long storyId);

	@Delete("DELETE FROM reports WHERE storyId = #{storyId}")
	void deleteReports(Long storyId);

	@Delete("DELETE FROM reports WHERE userId = #{userId}")
	void deleteUserReports(Long userId);
}
