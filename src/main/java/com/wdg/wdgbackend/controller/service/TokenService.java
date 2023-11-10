package com.wdg.wdgbackend.controller.service;

import com.wdg.wdgbackend.controller.util.CustomException;
import com.wdg.wdgbackend.model.entity.User;
import com.wdg.wdgbackend.model.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class TokenService {

	private static final Logger logger = LoggerFactory.getLogger(TokenService.class);
	private final UserRepository userRepository;
	@Value("${jwt.secret-key}")
	private String secretKeyProperty;
	private static Key secretKey;

	@Autowired
	public TokenService(UserRepository userRepository) {
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
				.claim("nickname", user.getNickname())
				.claim("platform", user.getPlatform())
				.claim("token-type", "access")
				.expiration(new Date(expirationTime))
				.signWith(secretKey)
				.compact();
	}

	public String generateRefreshToken(User user, long expirationTime) {
		return Jwts.builder()
				.subject(String.valueOf(user.getSnsId()))
				.claim("id", user.getId())
				.claim("nickname", user.getNickname())
				.claim("platform", user.getPlatform())
				.claim("token-type", "refresh")
				.expiration(new Date(expirationTime))
				.signWith(secretKey)
				.compact();
	}

	public Long getIdFromAccessToken(String authorizationHeader) throws CustomException {
		return Long.parseLong(getClaims(authorizationHeader).get("id").toString());
	}

	public String getNicknameFromAccessToken(String authorizationHeader) throws CustomException {
		return getClaims(authorizationHeader).get("nickname").toString();
	}

	public Long getSnsIdFromAccessToken(String authorizationHeader) throws CustomException {
		return Long.parseLong(getClaims(authorizationHeader).getSubject());
	}

	private static Claims getClaims(String authorizationHeader) {
		try {
			String accessToken = authorizationHeader.replace("Bearer ", "");
			return Jwts.parser().verifyWith((SecretKey) secretKey).build().parseSignedClaims(accessToken).getPayload();
		} catch (JwtException | IllegalArgumentException e) {
			log.error("JWT 파싱 과정 중 에러 발생", e);
			throw new CustomException("JWT parsing error", e, HttpStatus.UNAUTHORIZED);
		}
	}

	public Optional<User> validateToken(String token) {
		try {
			Jws<Claims> claimsJws = Jwts.parser().verifyWith((SecretKey) secretKey).build().parseSignedClaims(token);
			Claims claims = claimsJws.getPayload();
			Date expiration = claims.getExpiration();

			if (expiration.before(new Date())) return Optional.empty();

			Long snsId = Long.parseLong(claims.getSubject());
			return Optional.ofNullable(userRepository.findUserBySnsId(snsId));
		} catch (JwtException | IllegalArgumentException | DataAccessException e) {
			log.error("토큰 검증 과정 중 에러 발생", e);
			return Optional.empty();
		}
	}
}
