package com.wdg.wdgbackend.model.mapper;

import org.apache.ibatis.annotations.*;

@Mapper
public interface ReportMapper {

	@Select("SELECT EXISTS (SELECT 1 FROM reports WHERE userId = #{userId} AND storyId = #{storyId})")
	boolean isReported(long userId, long storyId);

	@Insert("INSERT INTO reports (userId, storyId) VALUES (#{userId}, #{storyId})")
	void insertReport(long userId, long storyId);

	@Select("SELECT reportNum FROM story WHERE id = #{storyId}")
	int getReportNum(long storyId);

	@Delete("DELETE FROM reports WHERE storyId = #{storyId}")
	void deleteReports(long storyId);

	@Delete("DELETE FROM reports WHERE userId = #{userId}")
	void deleteUserReports(long userId);
}
