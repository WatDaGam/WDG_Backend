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

	@Autowired
	public StoryController(StoryService storyService) {
		this.storyService = storyService;
	}

	@PostMapping("/upload")
	public ResponseEntity<String> upload(@RequestHeader("Authorization") String authorizationHeader, @RequestBody String jsonData) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			storyService.insertNewStory(authorizationHeader, objectMapper.readTree(jsonData));
		} catch (IOException e) {
			return new ResponseEntity<>("Invalid story", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Story uploaded successfully", HttpStatus.CREATED);
	}

	@GetMapping("/info")
	public ResponseEntity<String> info(@RequestParam("storyId") String storyId) {
		String storyInfoJSON = storyService.makeStoryJSONObject(Long.parseLong(storyId));
		return new ResponseEntity<>(storyInfoJSON, HttpStatus.OK);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<String> delete(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("storyId") String storyId) {
		storyService.deleteStory(authorizationHeader, storyId);
		return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);
	}
}
