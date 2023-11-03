package com.wdg.wdgbackend.controller.service;

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

	private static final int HOUR = 60;
	private static final long ACCESS_TOKEN_EXPIRY = 1000L * 60 * 10; // 10분
	private static final long REFRESH_TOKEN_EXPIRY = 1000L * 60 * 24 * HOUR * 30; // 24시간 * 30 = 1달

	private final UserRepository userRepository;
	private final JwtService jwtService;

	@Autowired
	public LoginService(UserRepository userRepository, JwtService jwtService) {
		this.userRepository = userRepository;
		this.jwtService = jwtService;
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
		long accessExpirationTime = System.currentTimeMillis() + ACCESS_TOKEN_EXPIRY;
		long refreshExpirationTime = System.currentTimeMillis() + REFRESH_TOKEN_EXPIRY;
		responseHeaders.add("Authorization", "Bearer " + jwtService.generateAccessToken(requestUser, accessExpirationTime));
		responseHeaders.add("Refresh-Token", jwtService.generateRefreshToken(requestUser, refreshExpirationTime));
		responseHeaders.add("Access-Expiration-Time", String.valueOf(accessExpirationTime));
		responseHeaders.add("Refresh-Expiration-Time", String.valueOf(refreshExpirationTime));
		return responseHeaders;
	}

	private User findUserBySnsId(long snsId) {
		return userRepository.findUserBySnsId(snsId);
	}

	public void insertUser(long snsId) {
		userRepository.insertUser(new User(0L, snsId, SNSPlatform.KAKAO.ordinal()));
	}
}
