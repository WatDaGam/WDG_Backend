package com.wdg.wdgbackend.controller.service;

import com.wdg.wdgbackend.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NicknameService {

	private final UserRepository userRepository;
	private final TokenService tokenService;

	@Autowired
	public NicknameService(UserRepository userRepository, TokenService tokenService) {
		this.userRepository = userRepository;
		this.tokenService = tokenService;
	}

	public boolean isNicknameDuplicated(String nickname) {
		return userRepository.isNicknameDup(nickname);
	}

	public void setNickname(Long id, String nickname) {
		userRepository.updateNicknameById(id, nickname);
	}
}
