package com.wdg.wdgbackend.model.repository;

import com.wdg.wdgbackend.model.entity.User;
import com.wdg.wdgbackend.model.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

	private final UserMapper userMapper;

	@Autowired
	public UserRepositoryImpl(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	@Override
	public boolean findSnsId(long snsId) {
		return userMapper.checkSnsId(snsId) == 1;
	}

	@Override
	public boolean isNicknameDup(String nickname) {
		return userMapper.checkNicknameDup(nickname) == 1;
	}

	@Override
	public boolean isNicknameNull(long snsId) {
		return userMapper.checkNicknameIsNull(snsId) == 0;
	}

	@Override
	public boolean isNicknameNullWithId(Long id) {
		return userMapper.checkNicknameIsNullWithId(id) == 0;
	}

	@Override
	public void insertUser(User user) {
		userMapper.insert(user);
	}

	@Override
	public User findUserById(Long id) {
		return userMapper.findUserById(id);
	}

	@Override
	public User findUserBySnsId(Long snsId) {
		return userMapper.findUserBySnsId(snsId);
	}

	@Override
	public void incrementStoryNum(Long userId) {
		userMapper.incrementStoryNum(userId);
	}

	@Override
	public void updateNicknameById(Long id, String nickname) {
		userMapper.updateNicknameById(id, nickname);
	}

	@Override
	public void deactivateUserById(Long id) {
		userMapper.deactivateUserById(id);
	}

	@Override
	public void deleteUserById(Long id) {
		userMapper.deleteUserById(id);
	}
}
