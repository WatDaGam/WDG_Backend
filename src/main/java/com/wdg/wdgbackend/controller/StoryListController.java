package com.wdg.wdgbackend.controller;

import com.wdg.wdgbackend.controller.service.StoryListService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@RequestMapping("/storyList")
public class StoryListController {

	private final StoryListService storyListService;

	@Autowired
	public StoryListController(StoryListService storyListService) {
		this.storyListService = storyListService;
	}

	@PostMapping("/renew")
	public ResponseEntity<String> renew(@RequestBody String jsonData) {
		JSONObject newListOfStory = storyListService.getNewListOfStory(jsonData);
		return new ResponseEntity<>(newListOfStory.toString(), HttpStatus.OK);
	}
}
