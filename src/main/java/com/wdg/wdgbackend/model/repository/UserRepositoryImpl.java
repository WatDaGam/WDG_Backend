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
	public boolean findSnsId(int snsId) {
		return userMapper.checkSnsId(snsId) == 1;
	}

	@Override
	public boolean isNicknameNull(int snsId) {
		return userMapper.checkNicknameIsNull(snsId) == 0;
	}

	@Override
	public void insertUser(User user) {
		userMapper.insert(user);
	}

	@Override
	public Long findUserIdByNickname(String nickname) {
		return userMapper.findUserIdByNickname(nickname);
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
