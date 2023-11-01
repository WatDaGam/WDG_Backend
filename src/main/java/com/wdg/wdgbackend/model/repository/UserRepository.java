package com.wdg.wdgbackend.model.repository;

import com.wdg.wdgbackend.model.entity.User;

public interface UserRepository {
	boolean findSnsId(long snsId);
	boolean isNicknameNull(long snsId);
	void insertUser(User user);

	User findUserBySnsId(Long snsId);
	Long findUserIdByNickname(String nickname);

	void deactivateUserById(Long id);
	void deleteUserById(Long id);
}
