package com.wdg.wdgbackend.model.repository;

import com.wdg.wdgbackend.model.entity.SNSPlatform;
import com.wdg.wdgbackend.model.entity.User;

public interface UserRepository {
	boolean findSnsId(long snsId);
	boolean isNicknameDup(String nickname);
	boolean isNicknameNull(long snsId);
	boolean isNicknameNullWithId(Long userId);
	boolean isUserExists(Long userId);
	void insertUser(User user);

	User findUserById(Long userId);
	User findUserBySnsId(Long snsId);

	void incrementStoryNum(Long userId);
	void decrementStoryNum(Long userId);
	void incrementLikeNum(Long writerId);
	void decrementLikeNum(Long userId);
	void clearReportedStories(Long userId);
	Integer lockUserLikeNum(Long writerId);
	void updateNicknameById(Long userId, String nickname);
	void deactivateUserById(Long userId);
	void deleteUserById(Long userId);
}
