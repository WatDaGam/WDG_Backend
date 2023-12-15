package com.wdg.wdgbackend.model.repository;

import com.wdg.wdgbackend.model.entity.SNSPlatform;
import com.wdg.wdgbackend.model.entity.User;

public interface UserRepository {
	boolean findSnsId(long snsId);
	boolean isNicknameDup(String nickname);
	boolean isNicknameNull(long snsId);
	boolean isNicknameNullWithId(long userId);
	boolean isUserExists(long userId);
	void insertUser(User user);

	User findUserById(long userId);
	User findUserBySnsId(long snsId);

	void incrementStoryNum(long userId);
	void decrementStoryNum(long userId);
	void incrementLikeNum(long writerId);
	void decrementLikeNum(long userId);
	void decrementLikeNumWhenStoryDeleted(long storyLikeNum, long userId);
	void incrementReportedStoryNum(long userId);
	void clearReportedStoryNum(long userId);
	int lockUserLikeNum(long writerId);
	int lockUserStoryNum(long userId);
	void updateNicknameById(long userId, String nickname);
	void deactivateUserById(long userId);
	void deleteUserById(long userId);
}
