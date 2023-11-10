package com.wdg.wdgbackend.controller;

import com.wdg.wdgbackend.controller.service.UserInfoService;
import com.wdg.wdgbackend.controller.util.CustomException;
import com.wdg.wdgbackend.controller.util.MyJSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return new ResponseEntity<>(MyJSON.message("Invalid or missing Authorization header"), HttpStatus.BAD_REQUEST);
		}

		try {
			String userInfoJSON = userInfoService.makeUserJSONObject(authorizationHeader);
			if (userInfoJSON == null) {
				return new ResponseEntity<>(MyJSON.message("User information not found"), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(userInfoJSON, HttpStatus.OK);
		} catch (CustomException e) {
			return new ResponseEntity<>(MyJSON.message(e.getMessage()), e.getStatus());
		}
	}
}
