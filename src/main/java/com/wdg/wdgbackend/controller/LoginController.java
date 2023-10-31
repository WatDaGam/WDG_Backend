package com.wdg.wdgbackend.controller;

import com.wdg.wdgbackend.controller.service.LoginService;
import com.wdg.wdgbackend.model.entity.SNSPlatform;
import org.springframework.beans.factory.annotation.Autowired;
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

		if (platform.equals(SNSPlatform.KAKAO)) snsId = loginService.getIdFromKakao(accessToken);

		if (loginService.snsExists(snsId)) {
			if (loginService.isNicknameNull(snsId)) return new ResponseEntity<>("No Nickname", HttpStatus.CREATED);
			else return new ResponseEntity<>("User already exists", HttpStatus.OK);
		}
		loginService.insertUser(snsId);
		return new ResponseEntity<>("New user", HttpStatus.CREATED);
	}
}
