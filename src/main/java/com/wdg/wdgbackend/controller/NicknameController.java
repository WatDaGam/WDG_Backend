package com.wdg.wdgbackend.controller;

import com.wdg.wdgbackend.controller.service.LoginService;
import com.wdg.wdgbackend.controller.service.NicknameService;
import com.wdg.wdgbackend.controller.service.TokenService;
import com.wdg.wdgbackend.controller.util.CustomException;
import com.wdg.wdgbackend.controller.util.MyJSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/nickname")
public class NicknameController {

	private final NicknameService nicknameService;
	private final LoginService loginService;
	private final TokenService tokenService;

	@Autowired
	public NicknameController(NicknameService nicknameService, LoginService loginService, TokenService tokenService) {
		this.nicknameService = nicknameService;
		this.loginService = loginService;
		this.tokenService = tokenService;
	}

	@PostMapping("/check")
	public ResponseEntity<String> checkNickname(@RequestBody String nickname) {
		if (nickname == null || nickname.trim().isEmpty()) {
			return new ResponseEntity<>(MyJSON.message("Invalid nickname"), HttpStatus.BAD_REQUEST);
		}

		try {
			if (nicknameService.isNicknameDuplicated(nickname)) {
				return new ResponseEntity<>(MyJSON.message("nickname duplicated"), HttpStatus.OK);
			}
			return new ResponseEntity<>(MyJSON.message("nickname not duplicated"), HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<>(MyJSON.message("Database error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/set")
	public ResponseEntity<String> setNickname(@RequestBody String nickname, @RequestHeader("Authorization") String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return new ResponseEntity<>(MyJSON.message("Invalid Authorization Header"), HttpStatus.BAD_REQUEST);
		}
		if (nickname == null || nickname.trim().isEmpty()) {
			return new ResponseEntity<>(MyJSON.message("Invalid nickname"), HttpStatus.BAD_REQUEST);
		}

		try {
			Long userId = tokenService.getIdFromAccessToken(authorizationHeader);
			Long snsId = tokenService.getSnsIdFromAccessToken(authorizationHeader);

			if (nicknameService.isNicknameDuplicated(nickname)) {
				return new ResponseEntity<>(MyJSON.message("nickname duplicated"), HttpStatus.OK);
			}
			nicknameService.setNickname(userId, nickname);
			HttpHeaders responseHeaders = loginService.getHttpHeaders(snsId);
			return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
		} catch (CustomException e) {
			return new ResponseEntity<>(MyJSON.message(e.getMessage()), e.getStatus());
		} catch (DataAccessException e) {
			return new ResponseEntity<>(MyJSON.message("Database error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
