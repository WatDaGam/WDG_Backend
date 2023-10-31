package com.wdg.wdgbackend.controller.service;

import com.wdg.wdgbackend.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetoutService {

	private final UserRepository userRepository;

	@Autowired
	public GetoutService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void deleteUser(String nickname) {
		long userId = userRepository.findUserIdByNickname(nickname);
		userRepository.deleteUserById(userId);
	}
}
