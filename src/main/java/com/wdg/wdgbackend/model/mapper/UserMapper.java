package com.wdg.wdgbackend.model.mapper;

import com.wdg.wdgbackend.model.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

	@Select("SELECT COUNT(*) FROM user WHERE sns_id = #{snsId}")
	int checkSnsId(long snsId);

	@Select("SELECT CASE WHEN nickname IS NULL THEN 0 ELSE 1 END FROM user WHERE sns_id = #{snsId}")
	int checkNicknameIsNull(long snsId);

	@Insert("INSERT INTO user (sns_id, sns_platform) " +
			"VALUES (#{snsId}, #{sns})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	void insert(User user);

	@Select("SELECT id FROM user WHERE nickname = #{nickname}")
	Long findUserIdByNickname(String nickname);

	@Update("UPDATE user SET is_active = FALSE WHERE id = #{id}")
	void deactivateUserById(Long id);

	@Delete("DELETE FROM user WHERE id = #{id}")
	void deleteUserById(Long id);
}
