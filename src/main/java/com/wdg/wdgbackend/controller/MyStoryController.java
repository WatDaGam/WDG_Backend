package com.wdg.wdgbackend.controller;

import com.wdg.wdgbackend.controller.service.MyStoryService;
import com.wdg.wdgbackend.controller.util.CustomException;
import com.wdg.wdgbackend.controller.util.MyJSON;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/myStory")
public class MyStoryController {

	private final MyStoryService myStoryService;

	@Autowired
	public MyStoryController(MyStoryService myStoryService) {
		this.myStoryService = myStoryService;
	}

	@GetMapping
	public ResponseEntity<String> myStory(@RequestHeader("Authorization") String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return new ResponseEntity<>(MyJSON.message("Invalid Authorization Header"), HttpStatus.BAD_REQUEST);
		}
		try {
			JSONObject myStories = myStoryService.getStories(authorizationHeader);
			return new ResponseEntity<>(myStories.toString(), HttpStatus.OK);
		} catch (CustomException e) {
			return new ResponseEntity<>(MyJSON.message(e.getMessage()), e.getStatus());
		}
	}
}
