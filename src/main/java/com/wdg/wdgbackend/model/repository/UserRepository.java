package com.wdg.wdgbackend.model.repository;

import com.wdg.wdgbackend.model.entity.SNSPlatform;
import com.wdg.wdgbackend.model.entity.User;

import java.util.Optional;

public interface UserRepository {
//	boolean findSnsId(String snsId);
	boolean isNicknameDup(String nickname);
	boolean isNicknameNull(String snsId);
	boolean isNicknameNullWithId(long userId);
	boolean isUserExists(long userId);
	void insertUser(User user);

	Optional<User> findUserById(long userId);
	Optional<User> findUserBySnsId(String snsId);

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
