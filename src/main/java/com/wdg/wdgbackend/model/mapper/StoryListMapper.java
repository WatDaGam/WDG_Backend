package com.wdg.wdgbackend.model.mapper;

import com.wdg.wdgbackend.model.entity.Story;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface StoryListMapper {

	@Select("WITH UserReportedStories AS ( " +
			"    SELECT storyId " +
			"    FROM reports " +
			"    WHERE userId = #{userId} " +
			"), " +
			"BlockedStories AS ( " +
			"    SELECT writerId " +
			"    FROM blocks " +
			"    WHERE userId = #{userId} " +
			"), " +
			"SortedStories AS ( " +
			"    SELECT s.id, s.userId, s.nickname, s.content, s.likeNum, s.createdAt, s.lati, s.longi, s.reportNum " +
			"    FROM story s " +
			"    ORDER BY ST_Distance_Sphere(s.location, POINT(#{longi}, #{lati})) ASC " +
			"    LIMIT #{initialLimit} " +
			") " +
			"SELECT ss.* " +
			"FROM SortedStories ss " +
			"LEFT JOIN UserReportedStories urs ON ss.id = urs.storyId " +
			"LEFT JOIN BlockedStories bs ON ss.userId = bs.writerId " +
			"WHERE urs.storyId IS NULL AND bs.writerId IS NULL " +
			"LIMIT #{finalLimit};")
	List<Story> selectNearestStories(
			long userId,
			double lati,
			double longi,
			int initialLimit,
			int finalLimit);

	@Update("UPDATE user SET renewNum = renewNum + 1 WHERE id = #{userId}")
	void incrementStoryNum(long userId);
}
