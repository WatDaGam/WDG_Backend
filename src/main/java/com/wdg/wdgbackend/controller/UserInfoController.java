package com.wdg.wdgbackend.controller;

import com.wdg.wdgbackend.controller.service.UserInfoService;
import com.wdg.wdgbackend.controller.util.CustomException;
import com.wdg.wdgbackend.controller.util.MyJSON;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
	@Operation(
			summary = "사용자 정보 조회",
			description = "엑세스 토큰을 통해 사용자의 상세 정보를 조회합니다.",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "사용자 정보 조회 성공",
							content = @Content(
									mediaType = "application/json",
									examples = @ExampleObject(
											name = "SuccessResponse",
											value = "{\"nickname\": \"userNickname\", \"storyNum\": 10, \"likeNum\": 5, \"createdAt\": \"timestamp\"}"
									)
							)
					),
					@ApiResponse(
							responseCode = "400",
							description = "잘못된 또는 누락된 인증 헤더",
							content = @Content(
									mediaType = "application/json",
									examples = @ExampleObject(
											name = "InvalidHeader",
											value = "{\"message\": \"Invalid or missing Authorization header\"}"
									)
							)
					),
					@ApiResponse(
							responseCode = "404",
							description = "사용자 정보를 찾을 수 없음",
							content = @Content(
									mediaType = "application/json",
									examples = @ExampleObject(
											name = "UserNotFound",
											value = "{\"message\": \"User information not found\"}"
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
											value = "{\"message\": \"Database error\"}"
									)
							)
					)
			}
	)
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
