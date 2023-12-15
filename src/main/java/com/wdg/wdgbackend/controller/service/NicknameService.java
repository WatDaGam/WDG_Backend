package com.wdg.wdgbackend.controller.service;

import com.wdg.wdgbackend.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class NicknameService {

	private final UserRepository userRepository;

	@Autowired
	public NicknameService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public boolean isNicknameDuplicated(String nickname) throws DataAccessException {
		return userRepository.isNicknameDup(nickname);
	}

	public void setNickname(long userId, String nickname) throws DataAccessException {
		userRepository.updateNicknameById(userId, nickname);
	}
}
