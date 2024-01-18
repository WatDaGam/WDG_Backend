package com.wdg.wdgbackend.model.repository;

import com.wdg.wdgbackend.model.entity.User;
import com.wdg.wdgbackend.model.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

	private final UserMapper userMapper;

	@Autowired
	public UserRepositoryImpl(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

//	@Override
//	public boolean findSnsId(long snsId) {
//		return userMapper.checkSnsId(snsId) == 1;
//	}

	@Override
	public boolean isNicknameDup(String nickname) {
		return userMapper.checkNicknameDup(nickname) == 1;
	}

	@Override
	public boolean isNicknameNull(String snsId) {
		return userMapper.checkNicknameIsNull(snsId) == 0;
	}

	@Override
	public boolean isNicknameNullWithId(long userId) {
		return userMapper.checkNicknameIsNullWithId(userId) == 0;
	}

	@Override
	public boolean isUserExists(long userId) {
		return userMapper.isUserExists(userId) == 1;
	}

	@Override
	public void insertUser(User user) {
		userMapper.insert(user);
	}

	@Override
	public Optional<User> findUserById(long userId) {
		return userMapper.findUserById(userId);
	}

	@Override
	public Optional<User> findUserBySnsId(String snsId) {
		return userMapper.findUserBySnsId(snsId);
	}

	@Override
	public void incrementStoryNum(long userId) {
		userMapper.incrementStoryNum(userId);
	}

	@Override
	public void decrementStoryNum(long userId) {
		userMapper.decrementStoryNum(userId);
	}

	@Override
	public void incrementLikeNum(long writerId) {
		userMapper.incrementLikeNum(writerId);
	}

	@Override
	public void decrementLikeNum(long userId) {
		userMapper.decrementLikeNum(userId);
	}

	@Override
	public void decrementLikeNumWhenStoryDeleted(long storyLikeNum, long userId) {
		userMapper.decrementLikeNumWhenStoryDeleted(storyLikeNum, userId);
	}

	@Override
	public void incrementReportedStoryNum(long userId) {
		userMapper.incrementReportedStoryNum(userId);
	}

	@Override
	public void clearReportedStoryNum(long userId) {
		userMapper.clearReportedStoryNum(userId);
	}

	@Override
	public int lockUserLikeNum(long writerId) {
		return userMapper.lockUserLikeNum(writerId);
	}

	@Override
	public int lockUserStoryNum(long userId) {
		return userMapper.lockUserStoryNum(userId);
	}

	@Override
	public void updateNicknameById(long userId, String nickname) {
		userMapper.updateNicknameById(userId, nickname);
	}

	@Override
	public void deactivateUserById(long userId) {
		userMapper.deactivateUserById(userId);
	}

	@Override
	public void deleteUserById(long userId) {
		userMapper.deleteUserById(userId);
	}
}
