package com.wdg.wdgbackend.controller.service;

import com.wdg.wdgbackend.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NicknameService {

	private final UserRepository userRepository;

	@Autowired
	public NicknameService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public boolean isNicknameDuplicated(String nickname) {
		return userRepository.isNicknameDup(nickname);
	}

	public void setNickname(Long userId, String nickname) {
		userRepository.updateNicknameById(userId, nickname);
	}
}
