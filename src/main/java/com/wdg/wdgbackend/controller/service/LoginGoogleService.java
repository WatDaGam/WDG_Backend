package com.wdg.wdgbackend.controller.service;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import com.wdg.wdgbackend.model.entity.SNSPlatform;
import com.wdg.wdgbackend.model.entity.User;
import com.wdg.wdgbackend.model.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.Map;
import java.util.Optional;

@Service
public class LoginGoogleService extends LoginService{

	@Value("${google.client.id}") private String GOOGLE_CLIENT_ID;
	@Value("${google.client.secret}") private String GOOGLE_CLIENT_SECRET;

	public LoginGoogleService(UserRepository userRepository, TokenService tokenService) {
		super(userRepository, tokenService);
	}

	public User loginWithAuthHeader(String authorizationHeader) {
		String code = authorizationHeader.replace("Bearer ", "");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> tokenRequest = new LinkedMultiValueMap<>();

		tokenRequest.add("client_id", GOOGLE_CLIENT_ID);
		tokenRequest.add("client_secret", GOOGLE_CLIENT_SECRET);
		tokenRequest.add("code", code);
		tokenRequest.add("grant_type", "authorization_code");
		tokenRequest.add("redirect_uri", "");

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(tokenRequest, headers);
//		System.out.println("\n\n\n\nrequest = " + request + "\n\n\n\n\n");
//		System.out.println("tokenRequest.client_secret = " + tokenRequest.get("client_secret") + "\n\n\n\n");

		RestTemplate restTemplate = new RestTemplate();
//		ResponseEntity<String> response = restTemplate.postForEntity(reqUrl, request, String.class);

		ResponseEntity<String> response = restTemplate.exchange(
				GOOGLE_API_SERVER,
				HttpMethod.POST,
				request,
				String.class
		);

//		System.out.println("\n\n\n\n\nresponse = " + response);

		JSONObject tokenResponse = new JSONObject(response.getBody());
		String idToken = tokenResponse.getString("id_token");

		try {
			JWT jwt = JWTParser.parse(idToken);
			Map<String, Object> userClaims = jwt.getJWTClaimsSet().toJSONObject();

			String snsId = "G_" + userClaims.get("sub").toString(); // User's unique identifier
			// String email = userClaims.get("email").toString(); // User's email

			Optional<User> userFromDB = getUserFromDB(snsId);
			if (userFromDB.isEmpty()) throw new IllegalStateException("No user found");

			return userFromDB.get();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected void insertUser(String snsId) {
		userRepository.insertUser(new User(0L, snsId, SNSPlatform.GOOGLE.ordinal(), System.currentTimeMillis()));
	}
}
