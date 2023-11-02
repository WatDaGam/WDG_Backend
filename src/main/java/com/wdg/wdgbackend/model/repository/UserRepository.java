package com.wdg.wdgbackend.model.repository;

import com.wdg.wdgbackend.model.entity.SNSPlatform;
import com.wdg.wdgbackend.model.entity.User;

public interface UserRepository {
	boolean findSnsId(long snsId);
	boolean isNicknameDup(String nickname);
	boolean isNicknameNull(long snsId);
	boolean isNicknameNullWithId(Long id);
	void insertUser(User user);

	User findUserBySnsId(Long snsId);
	Long findUserIdByNickname(String nickname);

	void updateNicknameById(Long id, String nickname);
	void deactivateUserById(Long id);
	void deleteUserById(Long id);
}
