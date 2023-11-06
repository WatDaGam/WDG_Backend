package com.wdg.wdgbackend.controller;

import com.wdg.wdgbackend.controller.service.TokenService;
import com.wdg.wdgbackend.controller.service.UserInfoService;
import com.wdg.wdgbackend.model.entity.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

	private final UserInfoService userInfoService;
	private final TokenService tokenServcie;

	@Autowired
	public UserInfoController(UserInfoService userInfoService, TokenService tokenServcie) {
		this.userInfoService = userInfoService;
		this.tokenServcie = tokenServcie;
	}

	@GetMapping
	public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String authorizationHeader) {
		Long idFromAccessToken = tokenServcie.getIdFromAccessToken(authorizationHeader);
		User user = userInfoService.getUser(idFromAccessToken);

		return ResponseEntity.ok(userInfoService.makeUserJSONObject(user).toString());
	}
}
