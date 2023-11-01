package com.wdg.wdgbackend.controller.util;

import com.wdg.wdgbackend.model.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {

	private static final int HOUR = 60;
	private static final Key SECRET_KEY = Jwts.SIG.HS256.key().build();
	private static final long ACCESS_TOKEN_EXPIRY = 1000 * 60 * 10; // 10분
	private static final long REFRESH_TOKEN_EXPIRY = 1000L * 60 * 24 * HOUR * 30; // 24시간 * 30 = 1달

	public static String generateAccessToken(User user) {

		return Jwts.builder()
				.subject(String.valueOf(user.getSnsId()))
				.claim("id", user.getId())
				.claim("sns", user.getSns())
				.claim("token-type", "access")
				.expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRY))
				.signWith(SECRET_KEY)
				.compact();
	}

	public static String generateRefreshToken(User user) {
		return Jwts.builder()
				.subject(String.valueOf(user.getSnsId()))
				.claim("id", user.getId())
				.claim("sns", user.getSns())
				.claim("token-type", "refresh")
				.expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRY))
				.signWith(SECRET_KEY)
				.compact();
	}

//	public static boolean validateToken(String accessToken) {
//		try {
//			Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).build().parseSignedClaims(accessToken);
//			Date expiration = claims.getPayload().getExpiration();
//
//			if (expiration.before(new Date())) {
//
//			}
//			return true;
//		} catch (JwtException | IllegalArgumentException exception) {
//			return false;
//		}
//	}
}
