package com.wdg.wdgbackend.model.mapper;

import com.wdg.wdgbackend.model.entity.Story;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StoryMapper {
	@Insert("INSERT INTO story (userId, nickname, content, createdAt, lati, longi, location) " +
			"VALUES (#{userId}, #{nickname}, #{content}, #{createdAt}, #{lati}, #{longi}, ST_PointFromText(CONCAT('POINT(', #{longi}, ' ', #{lati}, ')')))")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	void insert(Story story);

	@Update("UPDATE story SET likeNum = likeNum + 1 WHERE id = #{storyId}")
	void likePlus(long storyId);

	@Update("UPDATE story SET likeNum = likeNum - 1 WHERE id = #{storyId}")
	void likeMinus(long storyId);

	@Select("SELECT content FROM story WHERE id = #{storyId}")
	String getContent(long storyId);

	@Select("SELECT userId FROM story WHERE id = #{storyId}")
	long getUserIdFromStory(long storyId);

	@Select("SELECT likeNum FROM story WHERE id = #{storyId} FOR UPDATE")
	Integer lockStoryLikeNum(long storyId);

	@Select("SELECT id, userId, nickname, content, likeNum, createdAt ,lati, longi, reportNum from story WHERE id = #{storyId}")
	Story getStory(long storyId);

	@Select("SELECT likeNum from story WHERE id = #{storyId}")
	Integer getStoryLikeNum(long storyId);

	@Select("SELECT id, userId, nickname, content, likeNum, createdAt ,lati, longi FROM story " +
			"WHERE userId = #{userId} " +
			"ORDER BY createdAt ASC")
	List<Story> getStoryByUserId(long userId);

	@Delete("DELETE FROM story WHERE id = #{storyId}")
	void delete(long storyId);
}
