package com.wdg.wdgbackend.controller.service;

import com.wdg.wdgbackend.model.entity.SNSPlatform;
import com.wdg.wdgbackend.model.entity.User;
import com.wdg.wdgbackend.model.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class LoginKakaoService extends LoginService {

	public LoginKakaoService(UserRepository userRepository, TokenService tokenService) {
		super(userRepository, tokenService);
	}

	public User loginWithAuthHeader(String authorizationHeader) {
		String accessToken = authorizationHeader.replace("Bearer ", "");
		String snsId = "K_" + getIdFromAPIServer(makeHttpEntity(accessToken), KAKAO_API_SERVER);
		Optional<User> userFromDB = getUserFromDB(snsId);
		if (userFromDB.isEmpty()) throw new IllegalStateException("No user found");

		return userFromDB.get();
	}

	@Override
	protected void insertUser(String snsId) {
		userRepository.insertUser(new User(0L, snsId, SNSPlatform.KAKAO.ordinal(), System.currentTimeMillis()));
	}
}
