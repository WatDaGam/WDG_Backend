package com.wdg.wdgbackend.model.mapper;

import com.wdg.wdgbackend.model.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

	@Select("SELECT COUNT(*) FROM user WHERE sns_id = #{snsId}")
	int checkSnsId(long snsId);

	@Select("SELECT COUNT(*) FROM user WHERE nickname = #{nickname}")
	int checkNicknameDup(String nickname);

	@Select("SELECT CASE WHEN nickname IS NULL THEN 0 ELSE 1 END FROM user WHERE sns_id = #{snsId}")
	int checkNicknameIsNull(long snsId);

	@Select("SELECT CASE WHEN nickname IS NULL THEN 0 ELSE 1 END FROM user WHERE id = #{id}")
	int checkNicknameIsNullWithId(long id);

	@Insert("INSERT INTO user (sns_id, sns_platform) " +
			"VALUES (#{snsId}, #{sns})")
	@Results({
			@Result(property = "id", column = "id"),
			@Result(property = "snsId", column = "sns_id"),
			@Result(property = "sns", column = "sns_platform")
	})
	@Options(useGeneratedKeys = true, keyProperty = "id")
	void insert(User user);

	@Select("SELECT * FROM user WHERE id = #{id}")
	@Results({
			@Result(property = "id", column = "id"),
			@Result(property = "snsId", column = "sns_id"),
			@Result(property = "nickname", column = "nickname", javaType = String.class),
			@Result(property = "sns", column = "sns_platform"),
			@Result(property = "storyNum", column = "story_num"),
			@Result(property = "likeNum", column = "like_num"),
			@Result(property = "createdAt", column = "created_at"),
			@Result(property = "isActive", column = "is_active")
	})
	User findUserById(Long id);

	@Select("SELECT * FROM user WHERE sns_id = #{snsId}")
	@Results({
			@Result(property = "id", column = "id"),
			@Result(property = "snsId", column = "sns_id"),
			@Result(property = "nickname", column = "nickname", javaType = String.class),
			@Result(property = "sns", column = "sns_platform"),
			@Result(property = "storyNum", column = "story_num"),
			@Result(property = "likeNum", column = "like_num"),
			@Result(property = "createdAt", column = "created_at"),
			@Result(property = "isActive", column = "is_active")
	})
	User findUserBySnsId(Long snsId);

	@Update("UPDATE user SET nickname = #{nickname} WHERE id = #{id}")
	void updateNicknameById(@Param("id") Long id, @Param("nickname") String nickname);

	@Update("UPDATE user SET story_num = story_num + 1 WHERE id = #{userId}")
	void incrementStoryNum(Long userId);

	@Update("UPDATE user SET is_active = FALSE WHERE id = #{id}")
	void deactivateUserById(Long id);

	@Delete("DELETE FROM user WHERE id = #{id}")
	void deleteUserById(Long id);
}
