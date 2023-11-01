package com.wdg.wdgbackend.controller.service;

import com.wdg.wdgbackend.controller.util.JwtUtil;
import com.wdg.wdgbackend.model.entity.SNSPlatform;
import com.wdg.wdgbackend.model.entity.User;
import com.wdg.wdgbackend.model.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LoginService {

	private final UserRepository userRepository;

	@Autowired
	public LoginService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	private HttpEntity<String> makeHttpEntity(String accessToken) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Authorization", "Bearer " + accessToken);
		 return new HttpEntity<>("parameters", httpHeaders);
	}

	private long getKakaoIdNum(HttpEntity<String> httpResponse) {
		JSONObject jsonKakaoAccount = new JSONObject(httpResponse.getBody());
		return jsonKakaoAccount.getLong("id");
	}

	public long getIdFromKakao(String accessToken) {
		HttpEntity<String> httpEntityToKakao = makeHttpEntity(accessToken);

		String kakaoApiUrl = "https://kapi.kakao.com/v2/user/me";
		HttpEntity<String> httpResponse = new RestTemplate().exchange(kakaoApiUrl, HttpMethod.GET, httpEntityToKakao, String.class);

		return getKakaoIdNum(httpResponse);
	}

	public boolean snsExists(long snsId) {
		return userRepository.findSnsId(snsId);
	}

	private boolean isNicknameNull(long snsId) {
		return userRepository.isNicknameNull(snsId);
	}

	public boolean alreadySignedIn(long snsId) {
		return userRepository.findSnsId(snsId) && !userRepository.isNicknameNull(snsId);
	}

	public HttpHeaders getHttpHeaders(long snsId) {
		User requestUser = findUserBySnsId(snsId);

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Authorization", "Bearer " + JwtUtil.generateAccessToken(requestUser));
		responseHeaders.add("Refresh-Token", JwtUtil.generateRefreshToken(requestUser));
		return responseHeaders;
	}

	public void insertUser(long snsId) {
		userRepository.insertUser(new User(0L, snsId, SNSPlatform.KAKAO));
	}

	private User findUserBySnsId(long snsId) {
		return userRepository.findUserBySnsId(snsId);
	}
}
