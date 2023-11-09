package com.wdg.wdgbackend.model.mapper;

import com.wdg.wdgbackend.model.entity.Story;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface StoryListMapper {
	@Select("SELECT id, userId, nickname, content, likeNum ,lati, longi, createdAt FROM story " +
			"ORDER BY ST_Distance_Sphere(location, POINT(#{longitude}, #{latitude})) ASC " +
			"LIMIT #{limit}")
	List<Story> selectNearestStories(
			double latitude,
			double longitude
			,int limit);
}
