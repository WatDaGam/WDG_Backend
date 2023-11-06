package com.wdg.wdgbackend.model.mapper;

import com.wdg.wdgbackend.model.entity.Story;
import org.apache.ibatis.annotations.*;

@Mapper
public interface StoryMapper {
	@Insert("INSERT INTO story (userId, nickname, content, lati, longi) " +
			"VALUES (#{userId}, #{nickname}, #{content}, #{lati}, #{longi})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	void insert(Story story);

	@Update("UPDATE story SET like_num = like_num + 1 WHERE id = #{storyId}")
	void likePlus(Long storyId);

	@Update("UPDATE story SET like_num = like_num - 1 WHERE id = #{storyId}")
	void likeMinus(Long storyId);

	@Select("SELECT like_num FROM story WHERE id = #{storyId} FOR UPDATE")
	void lockStory(Long storyId);

	@Select("SELECT * from story WHERE id = #{storyId}")
	Story getStory(Long storyId);

	@Delete("DELETE FROM story WHERE id = #{storyId}")
	void delete(Long storyId);
}
