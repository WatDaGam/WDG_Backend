package com.wdg.wdgbackend.controller.service;

import com.wdg.wdgbackend.controller.util.CustomException;
import com.wdg.wdgbackend.model.entity.SNSPlatform;
import com.wdg.wdgbackend.model.entity.User;
import com.wdg.wdgbackend.model.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class LoginService {

	private static final long ACCESS_TOKEN_EXPIRY = 1000L * 60 * 10; // 10분
	private static final long REFRESH_TOKEN_EXPIRY = 1000L * 60 * 20; // 20분

	private final UserRepository userRepository;
	private final TokenService tokenService;

	@Autowired
	public LoginService(UserRepository userRepository, TokenService tokenService) {
		this.userRepository = userRepository;
		this.tokenService = tokenService;
	}

	private HttpEntity<String> makeHttpEntity(String accessToken) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Authorization", "Bearer " + accessToken);
		return new HttpEntity<>("parameters", httpHeaders);
	}

	private long getKakaoIdNum(HttpEntity<String> httpResponse) throws JSONException {
		JSONObject jsonKakaoAccount = new JSONObject(httpResponse.getBody());
		return jsonKakaoAccount.getLong("id");
	}

	public long getIdFromKakao(String accessToken) {
		HttpEntity<String> httpEntityToKakao = makeHttpEntity(accessToken);
		String kakaoApiUrl = "https://kapi.kakao.com/v2/user/me";

		try {
			HttpEntity<String> httpResponse = new RestTemplate().exchange(kakaoApiUrl, HttpMethod.GET, httpEntityToKakao, String.class);
			return getKakaoIdNum(httpResponse);
		} catch (HttpClientErrorException | HttpServerErrorException | ResourceAccessException e) {
			log.error("Kakao API 에러", e);
			throw new CustomException("Error occurred while using Kakao API", e, HttpStatus.BAD_REQUEST);
		} catch (JSONException e) {
			log.error("JSON 파싱 에러", e);
			throw new CustomException("JSON parsing error", e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public boolean snsExists(long snsId) throws DataAccessException {
		return userRepository.findSnsId(snsId);
	}

	public boolean alreadySignedIn(long snsId) throws DataAccessException {
		return userRepository.findSnsId(snsId) && !userRepository.isNicknameNull(snsId);
	}

	public void insertUser(long snsId) throws DataAccessException {
		userRepository.insertUser(new User(0L, snsId, SNSPlatform.KAKAO.ordinal()));
	}

	public HttpHeaders getHttpHeaders(long snsId) throws DataAccessException {
		User requestUser = findUserBySnsId(snsId);

		HttpHeaders responseHeaders = new HttpHeaders();
		long accessExpirationTime = System.currentTimeMillis() + ACCESS_TOKEN_EXPIRY;
		long refreshExpirationTime = System.currentTimeMillis() + REFRESH_TOKEN_EXPIRY;
		responseHeaders.add("Authorization", "Bearer " + tokenService.generateAccessToken(requestUser, accessExpirationTime));
		responseHeaders.add("Refresh-Token", tokenService.generateRefreshToken(requestUser, refreshExpirationTime));
		responseHeaders.add("Access-Expiration-Time", String.valueOf(accessExpirationTime));
		responseHeaders.add("Refresh-Expiration-Time", String.valueOf(refreshExpirationTime));
		return responseHeaders;
	}

	private User findUserBySnsId(long snsId) throws DataAccessException {
		return userRepository.findUserBySnsId(snsId);
	}
}
