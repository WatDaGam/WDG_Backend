package com.wdg.wdgbackend.model.mapper;

import com.wdg.wdgbackend.model.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

	@Select("SELECT COUNT(*) FROM user WHERE snsId = #{snsId}")
	int checkSnsId(long snsId);

	@Select("SELECT COUNT(*) FROM user WHERE nickname = #{nickname}")
	int checkNicknameDup(String nickname);

	@Select("SELECT COUNT(*) FROM user WHERE id = #{userId}")
	int isUserExists(long userId);

	@Select("SELECT CASE WHEN nickname IS NULL THEN 0 ELSE 1 END FROM user WHERE snsId = #{snsId}")
	int checkNicknameIsNull(long snsId);

	@Select("SELECT CASE WHEN nickname IS NULL THEN 0 ELSE 1 END FROM user WHERE id = #{userId}")
	int checkNicknameIsNullWithId(long userId);

	@Insert("INSERT INTO user (snsId, platform, createdAt) " +
			"VALUES (#{snsId}, #{platform}, #{createdAt})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	void insert(User user);

	@Select("SELECT * FROM user WHERE id = #{userId}")
	User findUserById(long userId);

	@Select("SELECT * FROM user WHERE snsId = #{snsId}")
	User findUserBySnsId(long snsId);

	@Update("UPDATE user SET nickname = #{nickname} WHERE id = #{userId}")
	void updateNicknameById(long userId, String nickname);

	@Update("UPDATE user SET storyNum = storyNum + 1 WHERE id = #{userId}")
	void incrementStoryNum(long userId);

	@Update("UPDATE user SET storyNum = storyNum - 1 WHERE id = #{userId}")
	void decrementStoryNum(long userId);

	@Update("UPDATE user SET likeNum = likeNum + 1 WHERE id = #{userId}")
	void incrementLikeNum(long userId);

	@Update("UPDATE user SET likeNum = likeNum - 1 WHERE id = #{userId}")
	void decrementLikeNum(long userId);

	@Update("UPDATE user SET likeNum = likeNum - #{storyLikeNum} WHERE id = #{userId}")
	void decrementLikeNumWhenStoryDeleted(long storyLikeNum, long userId);

	@Update("UPDATE user SET reportedStoryNum = reportedStoryNum + 1 WHERE id = #{userId}")
	void incrementReportedStoryNum(long userId);

	@Update("UPDATE user SET reportedStoryNum = 0 WHERE id = #{userId}")
	void clearReportedStoryNum(long userId);

	@Select("SELECT likeNum FROM user WHERE id = #{writerId} FOR UPDATE")
	int lockUserLikeNum(long writerId);

	@Select("SELECT storyNum FROM user WHERE id = #{userId} FOR UPDATE")
	int lockUserStoryNum(long userId);

	@Update("UPDATE user SET isActive = FALSE WHERE id = #{userId}")
	void deactivateUserById(long userId);

	@Delete("DELETE FROM user WHERE id = #{userId}")
	void deleteUserById(long userId);

}
