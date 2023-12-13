package com.wdg.wdgbackend.controller;

import com.wdg.wdgbackend.controller.service.StoryListService;
import com.wdg.wdgbackend.controller.util.CustomException;
import com.wdg.wdgbackend.controller.util.MyJSON;
import com.wdg.wdgbackend.model.entity.Story;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
	@Operation(
			summary = "스토리 리스트 리프레쉬",
			description = "사용자의 현재 위치를 기반으로 새로운 스토리 리스트를 가져옵니다.",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					required = true,
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(
									example = "{\"lati\": 37.5665, \"longi\": 126.9780}"
							)
					)
			),
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "스토리 리스트 리프레쉬 성공",
							content = @Content(
									mediaType = "application/json",
									examples = @ExampleObject(
											name = "SuccessResponse",
											value = "{\"stories\": [{\"id\": 1, \"content\": \"스토리 내용...\", \"lati\": 37.5665, \"longi\": 126.9780, ...}, {...}]}"
									)
							)
					),
					@ApiResponse(
							responseCode = "400",
							description = "잘못된 요청",
							content = @Content(
									mediaType = "application/json",
									examples = @ExampleObject(
											name = "BadRequest",
											value = "{\"message\": \"Invalid JSON format\"}"
									)
							)
					),
					@ApiResponse(
							responseCode = "500",
							description = "서버 내부 오류",
							content = @Content(
									mediaType = "application/json",
									examples = @ExampleObject(
											name = "ServerError",
											value = "{\"message\": \"Error occurred while retrieving story list\"}"
									)
							)
					)
			}
	)
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
