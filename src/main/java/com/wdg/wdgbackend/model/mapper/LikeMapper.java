package com.wdg.wdgbackend.model.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LikeMapper {

    @Insert("INSERT INTO likes (userId, storyId) VALUES (#{userId}, #{storyId})")
    void insertLikeNULLUser(long userId, long storyId);

    @Insert("INSERT INTO likes (userId, storyId, writerId) VALUES (#{userId}, #{storyId}, #{writerId})")
    void insertLike(long userId, long storyId, long writerId);


    @Select("SELECT EXISTS (SELECT 1 FROM likes WHERE userId = #{userId} AND storyId = #{storyId})")
    boolean isLiked(long userId, long storyId);

    @Delete("DELETE FROM likes WHERE storyId = #{storyId}")
    void deleteStoryLikes(long storyId);

    @Delete("DELETE FROM likes WHERE userId = #{userId}")
    void deleteUserLikes(long userId);
}
