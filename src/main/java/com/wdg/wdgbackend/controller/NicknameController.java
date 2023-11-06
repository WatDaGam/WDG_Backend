package com.wdg.wdgbackend.controller;

import com.wdg.wdgbackend.controller.service.TokenService;
import com.wdg.wdgbackend.controller.service.NicknameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nickname")
public class NicknameController {

	private final NicknameService nicknameService;
	private final TokenService tokenService;

	@Autowired
	public NicknameController(NicknameService nicknameService, TokenService tokenService) {
		this.nicknameService = nicknameService;
		this.tokenService = tokenService;
	}

	@PostMapping("/check")
	public ResponseEntity<String> checkNickname(@RequestBody String nickname) {
		if (nicknameService.isNicknameDuplicated(nickname)) return new ResponseEntity<>("duplicated", HttpStatus.OK);
		return new ResponseEntity<>("ok", HttpStatus.OK);
	}

	@PostMapping("/set")
	public ResponseEntity<String> setNickname(@RequestBody String nickname, @RequestHeader("Authorization") String authorizationHeader) {
		Long userId = tokenService.getIdFromAccessToken(authorizationHeader);
		if (nicknameService.isNicknameDuplicated(nickname)) return new ResponseEntity<>("duplicated", HttpStatus.OK);
		nicknameService.setNickname(userId, nickname);
		return new ResponseEntity<>("ok", HttpStatus.OK);
	}
}
