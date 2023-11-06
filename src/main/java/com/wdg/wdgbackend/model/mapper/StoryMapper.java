package com.wdg.wdgbackend.model.mapper;

import com.wdg.wdgbackend.model.entity.Story;
import org.apache.ibatis.annotations.*;

@Mapper
public interface StoryMapper {
	@Insert("INSERT INTO story (userId, content, lati, longi) " +
			"VALUES (#{userId}, #{content}, #{lati}, #{longi})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	void insert(Story story);


}
