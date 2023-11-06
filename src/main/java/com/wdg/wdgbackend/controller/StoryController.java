package com.wdg.wdgbackend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wdg.wdgbackend.controller.service.StoryService;
import com.wdg.wdgbackend.controller.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/story")
public class StoryController {

	private final StoryService storyService;
	private final TokenService tokenService;

	@Autowired
	public StoryController(StoryService storyService, TokenService tokenService) {
		this.storyService = storyService;
		this.tokenService = tokenService;
	}

	@PostMapping("/upload")
	public ResponseEntity<String> upload(@RequestHeader("Authorization") String authorizationHeader, @RequestBody String jsonData) {
		Long idFromAccessToken = tokenService.getIdFromAccessToken(authorizationHeader);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			storyService.insertNewStory(idFromAccessToken, objectMapper.readTree(jsonData));
		} catch (IOException e) {
			return new ResponseEntity<>("Invalid story", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Story uploaded successfully", HttpStatus.OK);
	}
}
