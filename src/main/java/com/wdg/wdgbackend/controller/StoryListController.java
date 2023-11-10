package com.wdg.wdgbackend.controller;

import com.wdg.wdgbackend.controller.service.StoryListService;
import com.wdg.wdgbackend.controller.util.CustomException;
import com.wdg.wdgbackend.controller.util.MyJSON;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
		if (jsonData == null || jsonData.trim().isEmpty()) {
			return new ResponseEntity<>(MyJSON.message("Invalid nickname"), HttpStatus.BAD_REQUEST);
		}
		try {
			JSONObject newListOfStory = storyListService.getNewListOfStory(jsonData);
			return new ResponseEntity<>(newListOfStory.toString(), HttpStatus.OK);
		} catch (CustomException e) {
			return new ResponseEntity<>(MyJSON.message(e.getMessage()), e.getStatus());
		}
	}
}
