package com.wdg.wdgbackend.controller.service;

import com.wdg.wdgbackend.model.entity.User;
import com.wdg.wdgbackend.model.repository.UserRepository;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class JwtService {

	private final UserRepository userRepository;
	@Value("${jwt.secret-key}")
	private String secretKeyProperty;
	private static Key secretKey;

	@Autowired
	public JwtService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@PostConstruct
	public void init() {
		secretKey = new SecretKeySpec(secretKeyProperty.getBytes(), "HmacSHA256");
	}

	public String generateAccessToken(User user, long expirationTime) {

		return Jwts.builder()
				.subject(String.valueOf(user.getSnsId()))
				.claim("id", user.getId())
				.claim("sns", user.getSns())
				.claim("token-type", "access")
				.expiration(new Date(expirationTime))
				.signWith(secretKey)
				.compact();
	}

	public String generateRefreshToken(User user, long expirationTime) {
		return Jwts.builder()
				.subject(String.valueOf(user.getSnsId()))
				.claim("id", user.getId())
				.claim("sns", user.getSns())
				.claim("token-type", "refresh")
				.expiration(new Date(expirationTime))
				.signWith(secretKey)
				.compact();
	}

	public Long getIdFromAccessToken(String accessToken) {
		Claims claims = Jwts.parser().verifyWith((SecretKey) secretKey).build().parseSignedClaims(accessToken).getPayload();
		return Long.parseLong(claims.get("id").toString());
	}

	public Optional<User> validateToken(String token) {
		try {
			Jws<Claims> claimsJws = Jwts.parser().verifyWith((SecretKey) secretKey).build().parseSignedClaims(token);
			Claims claims = claimsJws.getPayload();
			Date expiration = claims.getExpiration();

			if (expiration.before(new Date())) return Optional.empty();

			Long snsId = Long.parseLong(claims.getSubject());
			return Optional.ofNullable(userRepository.findUserBySnsId(snsId));
		} catch (JwtException | IllegalArgumentException e) {
			return Optional.empty();
		}
	}
}