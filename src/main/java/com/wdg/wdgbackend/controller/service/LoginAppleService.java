package com.wdg.wdgbackend.controller.service;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import com.wdg.wdgbackend.model.entity.SNSPlatform;
import com.wdg.wdgbackend.model.entity.User;
import com.wdg.wdgbackend.model.repository.UserRepository;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.FileReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public class LoginAppleService extends LoginService {

	@Value("${apple.team.id}") private String APPLE_TEAM_ID;
	@Value("${apple.client.id}") private String APPLE_CLIENT_ID;
	@Value("${apple.redirect.url}")	private String APPLE_REDIRECT_URL;
	@Value("${apple.key.id}") private String APPLE_KEY_ID;
	@Value("${apple.auth.key.path}") private String APPLE_AUTH_KEY_PATH;


	public LoginAppleService(UserRepository userRepository, TokenService tokenService) {
		super(userRepository, tokenService);
	}

	@Override
	public User loginWithAuthHeader(String authorizationHeader) {

		String code = authorizationHeader.replace("Bearer ", "");
		String clientSecret = createAppleSecret();

		String reqUrl = APPLE_AUTH_URL + "/auth/token";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> tokenRequest = new LinkedMultiValueMap<>();

		tokenRequest.add("client_id", APPLE_CLIENT_ID);
		tokenRequest.add("client_secret", clientSecret);
		tokenRequest.add("code", code);
		tokenRequest.add("grant_type", "authorization_code");
		tokenRequest.add("redirect_uri", APPLE_REDIRECT_URL);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(tokenRequest, headers);

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<String> response = restTemplate.exchange(
				reqUrl,
				HttpMethod.POST,
				request,
				String.class
		);

		JSONObject tokenResponse = new JSONObject(response.getBody());
		String idToken = tokenResponse.getString("id_token");

		try {
			JWT jwt = JWTParser.parse(idToken);
			Map<String, Object> userClaims = jwt.getJWTClaimsSet().toJSONObject();

			String snsId = "A_" + userClaims.get("sub").toString();

			Optional<User> userFromDB = getUserFromDB(snsId);
			if (userFromDB.isEmpty()) throw new IllegalStateException("No user found");

			return userFromDB.get();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}


	public String createAppleSecret() {
		JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256).keyID(APPLE_KEY_ID).build();
		Date now = new Date();

		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
				.issuer(APPLE_TEAM_ID)
				.issueTime(now)
				.expirationTime(new Date(now.getTime() + 600000)) // 현재 시간으로부터 10분 후
				.audience(APPLE_AUTH_URL)
				.subject(APPLE_CLIENT_ID)
				.build();


		SignedJWT jwt = new SignedJWT(header, claimsSet);
		try {
			PrivateKey privateKey = readPrivateKey(APPLE_AUTH_KEY_PATH);
			if (!(privateKey instanceof ECPrivateKey)) {
				throw new InvalidKeyException("Key is not an EC private key.");
			}
			ECPrivateKey ecPrivateKey = (ECPrivateKey) privateKey;
			JWSSigner jwsSigner = new ECDSASigner(ecPrivateKey);

			jwt.sign(jwsSigner);
		} catch (Exception e) {
			e.printStackTrace();
		}


		return jwt.serialize();
	}

	private PrivateKey readPrivateKey(String keyPath) throws Exception {
		try (PemReader pemReader = new PemReader(new FileReader(keyPath))) {
			PemObject pemObject = pemReader.readPemObject();
			byte[] keyBytes = pemObject.getContent();
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("EC");
			return keyFactory.generatePrivate(keySpec);
		}
	}

	@Override
	protected void insertUser(String snsId) {
		userRepository.insertUser(new User(0L, snsId, SNSPlatform.APPLE.ordinal(), System.currentTimeMillis()));
	}
}
