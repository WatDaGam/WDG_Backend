package com.wdg.wdgbackend.controller;

import com.wdg.wdgbackend.controller.service.LoginService;
import com.wdg.wdgbackend.model.entity.SNSPlatform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
		String accessToken = authorizationHeader.replace("Bearer ", "");

		try {
			if (platform.equals(SNSPlatform.KAKAO)) snsId = loginService.getIdFromKakao(accessToken);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (!loginService.snsExists(snsId)) loginService.insertUser(snsId);

		HttpHeaders responseHeaders = loginService.getHttpHeaders(snsId);

		if (loginService.alreadySignedIn(snsId)) return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
		return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
	}
}
