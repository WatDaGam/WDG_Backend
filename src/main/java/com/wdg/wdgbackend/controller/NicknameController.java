package com.wdg.wdgbackend.controller;

import com.wdg.wdgbackend.controller.service.JwtService;
import com.wdg.wdgbackend.controller.service.NicknameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nickname")
public class NicknameController {

	private final NicknameService nicknameService;
	private final JwtService jwtService;

	@Autowired
	public NicknameController(NicknameService nicknameService, JwtService jwtService) {
		this.nicknameService = nicknameService;
		this.jwtService = jwtService;
	}

	@PostMapping("/check")
	public ResponseEntity<String> checkNickname(@RequestBody String nickname) {
		if (nicknameService.isNicknameDuplicated(nickname)) return new ResponseEntity<>("duplicated", HttpStatus.OK);
		return new ResponseEntity<>("ok", HttpStatus.OK);
	}

	@PostMapping("/set")
	public ResponseEntity<String> setNickname(@RequestBody String nickname, @RequestHeader("Authorization") String authorizationHeader) {
		String accessToken = authorizationHeader.replace("Bearer ", "");
		Long idFromAccessToken = jwtService.getIdFromAccessToken(accessToken);
		if (nicknameService.isNicknameDuplicated(nickname)) return new ResponseEntity<>("duplicated", HttpStatus.OK);
		nicknameService.setNickname(idFromAccessToken, nickname);
		return new ResponseEntity<>("ok", HttpStatus.OK);
	}
}
