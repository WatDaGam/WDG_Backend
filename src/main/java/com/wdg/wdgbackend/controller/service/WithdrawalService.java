package com.wdg.wdgbackend.controller.service;

import com.wdg.wdgbackend.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WithdrawalService {

	private final UserRepository userRepository;
	private final StoryLikeCommonService storyLikeCommonService;

	@Autowired
	public WithdrawalService(UserRepository userRepository, StoryLikeCommonService storyLikeCommonService) {
		this.userRepository = userRepository;
		this.storyLikeCommonService = storyLikeCommonService;
	}

	public void deleteUser(Long userId) {
		storyLikeCommonService.deleteUserLikes(userId);
		userRepository.deleteUserById(userId);
	}
}
