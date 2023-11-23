package com.wdg.wdgbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wdg.wdgbackend.controller.service.StoryService;
import com.wdg.wdgbackend.controller.util.CustomException;
import com.wdg.wdgbackend.controller.util.MyJSON;
import com.wdg.wdgbackend.model.entity.Story;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/story")
public class StoryController {

	private final StoryService storyService;

	@Autowired
	public StoryController(StoryService storyService) {
		this.storyService = storyService;
	}

	@PostMapping("/upload")
	@Operation(
			summary = "스토리 업로드",
			description = "새로운 스토리를 업로드합니다.",
			requestBody = @RequestBody(
					required = true,
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = Story.class),
							examples = @ExampleObject(
									name = "StoryUploadExample",
									summary = "스토리 업로드 예시",
									value = "{\"lati\": 37.5665, \"longi\": 126.9780, \"content\": \"여기는 서울!\"}"
							)
					)
			),
			responses = {
					@ApiResponse(
							responseCode = "201",
							description = "스토리 업로드 성공",
							content = @Content(
									mediaType = "application/json",
									examples = @ExampleObject(
											name = "SuccessResponse",
											value = "{\"message\": \"Story uploaded successfully\"}"
									)
							)
					),
					@ApiResponse(
							responseCode = "500",
							description = "서버 에러",
							content = @Content(
									mediaType = "application/json",
									examples = @ExampleObject(
											name = "BadServer",
											value = "{\"message\": \"Error occurred while inserting a story\"}"
									)
							)
					)
			}
	)
	public ResponseEntity<String> upload(@RequestHeader("Authorization") String authorizationHeader, @RequestBody String jsonData) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return new ResponseEntity<>(MyJSON.message("Invalid Authorization Header"), HttpStatus.BAD_REQUEST);
		}
		if (jsonData == null || jsonData.trim().isEmpty()) {
			return new ResponseEntity<>(MyJSON.message("Invalid nickname"), HttpStatus.BAD_REQUEST);
		}

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			storyService.insertNewStory(authorizationHeader, objectMapper.readTree(jsonData));
		} catch (IOException | CustomException e) {
			return new ResponseEntity<>(MyJSON.message(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(MyJSON.message("Story uploaded successfully"), HttpStatus.CREATED);
	}

	@GetMapping("/info")
	@Operation(
			summary = "스토리 정보 조회",
			description = "특정 스토리의 상세 정보를 조회합니다.",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "스토리 정보 조회 성공",
							content = @Content(
									mediaType = "application/json",
									examples = @ExampleObject(
											name = "SuccessResponse",
											value = "{\"storyId\": 1, \"nickname\": 1, \"content\": \"이야기 내용...\", \"likeNum\": 1, \"createdAt\": 1269780}"
									)
							)
					),
					@ApiResponse(
							responseCode = "404",
							description = "스토리를 찾을 수 없음",
							content = @Content(
									mediaType = "application/json",
									examples = @ExampleObject(
											name = "NotFoundResponse",
											value = "{\"message\": \"Invalid story\"}"
									)
							)
					),
					@ApiResponse(
					responseCode = "400",
					description = "스토리 id 에러",
					content = @Content(
							mediaType = "application/json",
							examples = @ExampleObject(
									name = "NotFoundResponse",
									value = "{\"message\": \"storyId Number format error\"}"
							)
					)
			)
			}
	)
	public ResponseEntity<String> info(@RequestParam("storyId") String storyId) {
		try {
			String storyInfoJSON = storyService.makeStoryJSONObject(storyId);
			return new ResponseEntity<>(storyInfoJSON, HttpStatus.OK);
		} catch (CustomException e) {
			return new ResponseEntity<>(MyJSON.message(e.getMessage()), e.getStatus());
		}
	}

	@DeleteMapping("/delete")
	@Operation(
			summary = "스토리 삭제",
			description = "특정 스토리를 삭제합니다.",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "스토리 삭제 성공",
							content = @Content(
									mediaType = "application/json",
									examples = @ExampleObject(
											name = "SuccessResponse",
											value = "{\"message\": \"Deleted Successfully\"}"
									)
							)
					),
					@ApiResponse(
							responseCode = "404",
							description = "잘못된 요청",
							content = @Content(
									mediaType = "application/json",
									examples = @ExampleObject(
											name = "BadRequestResponse",
											value = "{\"message\": \"Error occurred while deleting a story\"}"
									)
							)
					),
					@ApiResponse(
							responseCode = "500",
							description = "데이터베이스 에러",
							content = @Content(
									mediaType = "application/json",
									examples = @ExampleObject(
											name = "BadDatabase",
											value = "{\"message\": \"Database error occurred while deleting a story\"}"
									)
							)
					)
			}
	)
	public ResponseEntity<String> delete(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("storyId") String storyId) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return new ResponseEntity<>(MyJSON.message("Invalid Authorization Header"), HttpStatus.BAD_REQUEST);
		}
		try {
			storyService.deleteStory(authorizationHeader, storyId);
			return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);
		} catch (CustomException e) {
			return new ResponseEntity<>(MyJSON.message(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
}
