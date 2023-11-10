package com.wdg.wdgbackend.model.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LikeMapper {
    @Insert("INSERT INTO likes (userId, storyId) VALUES (#{userId}, #{storyId})")
    void insertLike(Long userId, Long storyId);

    @Select("SELECT EXISTS (SELECT 1 FROM likes WHERE userId = #{userId} AND storyId = #{storyId})")
    boolean isLiked(Long userId, Long storyId);

    @Delete("DELETE FROM likes WHERE storyId = #{storyId}")
    void deleteStoryLikes(Long storyId);

    @Delete("DELETE FROM likes WHERE userId = #{userID}")
    void deleteUserLikes(Long userId);
}
