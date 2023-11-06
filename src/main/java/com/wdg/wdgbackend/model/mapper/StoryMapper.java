package com.wdg.wdgbackend.model.mapper;

import com.wdg.wdgbackend.model.entity.Story;
import org.apache.ibatis.annotations.*;

@Mapper
public interface StoryMapper {
	@Insert("INSERT INTO story (userId, nickname, content, lati, longi) " +
			"VALUES (#{userId}, #{nickname}, #{content}, #{lati}, #{longi})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	void insert(Story story);

	@Update("UPDATE story SET likeNum = likeNum + 1 WHERE id = #{storyId}")
	void likePlus(Long storyId);

	@Update("UPDATE story SET likeNum = likeNum - 1 WHERE id = #{storyId}")
	void likeMinus(Long storyId);

	@Select("SELECT likeNum FROM story WHERE id = #{storyId} FOR UPDATE")
	Integer lockStory(Long storyId);

	@Select("SELECT * from story WHERE id = #{storyId}")
	Story getStory(Long storyId);

	@Delete("DELETE FROM story WHERE id = #{storyId}")
	void delete(Long storyId);
}
