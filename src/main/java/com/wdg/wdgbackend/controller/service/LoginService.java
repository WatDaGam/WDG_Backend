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
		long id = jsonKakaoAccount.getLong("id");
		System.out.println("id = " + id);
		return id;
	}

	public long getIdFromKakao(String accessToken) {
		HttpEntity<String> httpEntityToKakao = makeHttpEntity(accessToken);

		String kakaoApiUrl = "https://kapi.kakao.com/v2/user/me";
		HttpEntity<String> httpResponse = new RestTemplate().exchange(kakaoApiUrl, HttpMethod.GET, httpEntityToKakao, String.class);

		return getKakaoIdNum(httpResponse);
	}

	public boolean snsExists(long kakaoIdNum) {
		return userRepository.findSnsId(kakaoIdNum);
	}

	public boolean isNicknameNull(long kakaoIdNum) {
		return userRepository.isNicknameNull(kakaoIdNum);
	}

	public void insertUser(long kakaoIdNum) {
		userRepository.insertUser(new User(0L, kakaoIdNum, SNSPlatform.KAKAO));
	}
}
