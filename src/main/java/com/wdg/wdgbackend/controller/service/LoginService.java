package com.wdg.wdgbackend.controller.service;

import com.wdg.wdgbackend.controller.util.CustomException;
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

import java.util.Optional;

@Slf4j
@Service
public abstract class LoginService {

	protected static final String KAKAO_API_SERVER = "https://kapi.kakao.com/v2/user/me";
//	protected static final String GOOGLE_API_SERVER = "https://www.googleapis.com/oauth2/v3/userinfo";
	protected static final String GOOGLE_API_SERVER = "https://oauth2.googleapis.com/token";
	protected static final String APPLE_AUTH_URL = "https://appleid.apple.com";

	protected final UserRepository userRepository;
	protected final TokenService tokenService;

	@Autowired
	public LoginService(UserRepository userRepository, TokenService tokenService) {
		this.userRepository = userRepository;
		this.tokenService = tokenService;
	}

	protected HttpEntity<String> makeHttpEntity(String accessToken) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Authorization", "Bearer " + accessToken);
		return new HttpEntity<>("parameters", httpHeaders);
	}

	protected String getIdFromAPIServer(HttpEntity<String> httpEntity, String APIServer) {

		try {
			HttpEntity<String> httpResponse = new RestTemplate().exchange(APIServer, HttpMethod.GET, httpEntity, String.class);
			return getIdFromToken(httpResponse);
		} catch (HttpClientErrorException | HttpServerErrorException | ResourceAccessException e) {
			log.error("Kakao API 에러", e);
			throw new CustomException("Error occurred while using Kakao API", e, HttpStatus.BAD_REQUEST);
		} catch (JSONException e) {
			log.error("JSON 파싱 에러", e);
			throw new CustomException("JSON parsing error", e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	protected String getIdFromToken(HttpEntity<String> httpResponse) throws JSONException {
		JSONObject jsonAccount = new JSONObject(httpResponse.getBody());
		return String.valueOf(jsonAccount.getLong("id"));
	}

	public Optional<User> getUserFromDB(String snsId) {
		Optional<User> userFromDB = userRepository.findUserBySnsId(snsId);
		if (userFromDB.isEmpty()) {
			insertUser(snsId);
			userFromDB = userRepository.findUserBySnsId(snsId);
		}
		return userFromDB;
	}

	public HttpHeaders getHttpHeaders(User userFromDB) {

		HttpHeaders responseHeaders = new HttpHeaders();
		long ACCESS_TOKEN_EXPIRY = 1000L * 60 * 60 * 24 * 7; // 1주일
		long accessExpirationTime = System.currentTimeMillis() + ACCESS_TOKEN_EXPIRY;
		long REFRESH_TOKEN_EXPIRY = ACCESS_TOKEN_EXPIRY * 4; // 1달
		long refreshExpirationTime = System.currentTimeMillis() + REFRESH_TOKEN_EXPIRY;

		responseHeaders.add("Authorization", "Bearer " + tokenService.generateAccessToken(userFromDB, accessExpirationTime));
		responseHeaders.add("Refresh-Token", tokenService.generateRefreshToken(userFromDB, refreshExpirationTime));
		responseHeaders.add("Access-Expiration-Time", String.valueOf(accessExpirationTime));
		responseHeaders.add("Refresh-Expiration-Time", String.valueOf(refreshExpirationTime));
		return responseHeaders;
	}

	public boolean alreadySignedIn(User user) throws DataAccessException {
		return !userRepository.isNicknameNull(user.getSnsId());
	}

	public abstract User loginWithAuthHeader(String authorizationHeader);

	protected abstract void insertUser(String snsId);
}
