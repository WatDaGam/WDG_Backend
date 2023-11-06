package com.wdg.wdgbackend.controller;

import com.wdg.wdgbackend.controller.service.TokenService;
import com.wdg.wdgbackend.controller.service.UserInfoService;
import com.wdg.wdgbackend.model.entity.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

	private final UserInfoService userInfoService;

	@Autowired
	public UserInfoController(UserInfoService userInfoService) {
		this.userInfoService = userInfoService;
	}

	@GetMapping
	public ResponseEntity<String> getUserInfo(@RequestHeader("Authorization") String authorizationHeader) {
		String userInfoJSON = userInfoService.makeUserJSONObject(authorizationHeader);
		return new ResponseEntity<>(userInfoJSON, HttpStatus.OK);
	}
}
