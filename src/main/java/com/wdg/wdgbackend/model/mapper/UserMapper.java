package com.wdg.wdgbackend.model.mapper;

import com.wdg.wdgbackend.model.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

	@Select("SELECT COUNT(*) FROM user WHERE snsId = #{snsId}")
	int checkSnsId(long snsId);

	@Select("SELECT COUNT(*) FROM user WHERE nickname = #{nickname}")
	int checkNicknameDup(String nickname);

	@Select("SELECT CASE WHEN nickname IS NULL THEN 0 ELSE 1 END FROM user WHERE snsId = #{snsId}")
	int checkNicknameIsNull(long snsId);

	@Select("SELECT CASE WHEN nickname IS NULL THEN 0 ELSE 1 END FROM user WHERE id = #{userId}")
	int checkNicknameIsNullWithId(long userId);

	@Insert("INSERT INTO user (snsId, platform, createdAt) " +
			"VALUES (#{snsId}, #{platform}, #{createdAt})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	void insert(User user);

	@Select("SELECT * FROM user WHERE id = #{userId}")
	User findUserById(Long userId);

	@Select("SELECT * FROM user WHERE snsId = #{snsId}")
	User findUserBySnsId(Long snsId);

	@Update("UPDATE user SET nickname = #{nickname} WHERE id = #{userId}")
	void updateNicknameById(Long userId, String nickname);

	@Update("UPDATE user SET storyNum = storyNum + 1 WHERE id = #{userId}")
	void incrementStoryNum(Long userId);

	@Update("UPDATE user SET storyNum = storyNum - 1 WHERE id = #{userId}")
	void decrementStoryNum(Long userId);

	@Update("UPDATE user SET likeNum = likeNum + 1 WHERE id = #{userId}")
	void incrementLikeNum(Long userId);

	@Update("UPDATE user SET likeNum = likeNum - 1 WHERE id = #{userId}")
	void decrementLikeNum(Long userId);

	@Select("SELECT likeNum FROM user WHERE id = #{writerId} FOR UPDATE")
	Integer lockUserLikeNum(Long writerId);

	@Update("UPDATE user SET isActive = FALSE WHERE id = #{userId}")
	void deactivateUserById(Long userId);

	@Delete("DELETE FROM user WHERE id = #{userId}")
	void deleteUserById(Long userId);

}
