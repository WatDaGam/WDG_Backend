package com.wdg.wdgbackend.controller;

import com.wdg.wdgbackend.controller.service.MyStoryService;
import com.wdg.wdgbackend.controller.util.CustomException;
import com.wdg.wdgbackend.controller.util.MyJSON;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
	@Operation(
			summary = "내 스토리 조회",
			description = "사용자의 스토리 목록을 조회합니다.",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "스토리 목록 조회 성공",
							content = @Content(
									mediaType = "application/json",
									examples = @ExampleObject(
											name = "SuccessResponse",
											value = "{\"stories\": [{\"id\": 1, \"content\": \"스토리 내용...\", ...}, {...}]}"
									)
							)
					),
					@ApiResponse(
							responseCode = "400",
							description = "잘못된 요청",
							content = @Content(
									mediaType = "application/json",
									examples = @ExampleObject(
											name = "BadRequestResponse",
											value = "{\"message\": \"Invalid Authorization Header\"}"
									)
							)
					),
					@ApiResponse(
							responseCode = "500",
							description = "서버 내부 오류",
							content = @Content(
									mediaType = "application/json",
									examples = @ExampleObject(
											name = "ServerErrorResponse",
											value = "{\"message\": \"Database error\"}"
									)
							)
					)
			}
	)
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
