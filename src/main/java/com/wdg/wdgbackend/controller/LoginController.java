package com.wdg.wdgbackend.controller;

import com.wdg.wdgbackend.controller.service.LoginService;
import com.wdg.wdgbackend.controller.util.CustomException;
import com.wdg.wdgbackend.controller.util.MyJSON;
import com.wdg.wdgbackend.model.entity.SNSPlatform;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {

	private final LoginService loginService;

	@Autowired
	public LoginController(LoginService loginService) {
		this.loginService = loginService;
	}

	@GetMapping
	public ResponseEntity<String> login(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("platform") SNSPlatform platform) {
		long snsId = 0;
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return new ResponseEntity<>(MyJSON.message("Invalid Authorization Header"), HttpStatus.BAD_REQUEST);
		}
		if (platform == null || !platform.isValid()) {
			return new ResponseEntity<>(MyJSON.message("Invalid platform"), HttpStatus.BAD_REQUEST);
		}

		String accessToken = authorizationHeader.replace("Bearer ", "");

		try {
			if (platform.equals(SNSPlatform.KAKAO)) snsId = loginService.getIdFromKakao(accessToken);
			if (!loginService.snsExists(snsId)) loginService.insertUser(snsId);
			HttpHeaders responseHeaders = loginService.getHttpHeaders(snsId);
			if (loginService.alreadySignedIn(snsId)) return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
			return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
		} catch (CustomException e) {
			return new ResponseEntity<>(MyJSON.message(e.getMessage()), e.getStatus());
		} catch (DataAccessException e) {
			return new ResponseEntity<>(MyJSON.message("Database error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
