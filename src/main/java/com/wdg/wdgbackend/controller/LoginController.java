package com.wdg.wdgbackend.controller;

import com.wdg.wdgbackend.controller.service.LoginAppleService;
import com.wdg.wdgbackend.controller.service.LoginGoogleService;
import com.wdg.wdgbackend.controller.service.LoginKakaoService;
import com.wdg.wdgbackend.controller.service.LoginService;
import com.wdg.wdgbackend.controller.util.CustomException;
import com.wdg.wdgbackend.controller.util.MyJSON;
import com.wdg.wdgbackend.model.entity.SNSPlatform;
import com.wdg.wdgbackend.model.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {

	private final LoginKakaoService loginKakaoService;
	private final LoginGoogleService loginGoogleService;
	private final LoginAppleService loginAppleRealService;

	@Autowired
	public LoginController(LoginKakaoService loginKakaoService, LoginGoogleService loginGoogleService, LoginAppleService loginAppleRealService) {
		this.loginKakaoService = loginKakaoService;
		this.loginGoogleService = loginGoogleService;
		this.loginAppleRealService = loginAppleRealService;
	}

	@GetMapping
	@Operation(
			summary = "로그인",
			description = "SNS 플랫폼을 통한 로그인을 처리합니다.",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "이미 가입된 사용자 로그인 성공",
							content = @Content(
									mediaType = "application/json",
									examples = @ExampleObject(
											name = "AlreadySignedInResponse",
											value = "{\"message\": \"Login successful\", \"Authorization\": \"Bearer token\", \"Refresh-Token\": \"refresh token\"}"
									)
							)
					),
					@ApiResponse(
							responseCode = "201",
							description = "새로운 사용자 로그인 성공",
							content = @Content(
									mediaType = "application/json",
									examples = @ExampleObject(
											name = "NewUserResponse",
											value = "{\"message\": \"User created\", \"Authorization\": \"Bearer token\", \"Refresh-Token\": \"refresh token\"}"
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
											value = "{\"error\": \"Invalid Authorization Header or platform\"}"
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
											value = "{\"error\": \"Database error\"}"
									)
							)
					)
			}
	)
	public ResponseEntity<String> login(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("platform") SNSPlatform platform) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return new ResponseEntity<>(MyJSON.message("Invalid Authorization Header"), HttpStatus.BAD_REQUEST);
		}
		if (platform == null || !platform.isValid()) {
			return new ResponseEntity<>(MyJSON.message("Invalid platform"), HttpStatus.BAD_REQUEST);
		}

		LoginService loginService;
		if (platform == SNSPlatform.KAKAO) loginService = loginKakaoService;
		else if (platform == SNSPlatform.GOOGLE) {
			loginService = loginGoogleService;
		} else {
			loginService = loginAppleRealService;
		}

		try {
			User loginUser = loginService.loginWithAuthHeader(authorizationHeader);
			HttpHeaders responseHeaders = loginService.getHttpHeaders(loginUser);

			if (loginService.alreadySignedIn(loginUser)) return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
			return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
		} catch (CustomException e) {
			return new ResponseEntity<>(MyJSON.message(e.getMessage()), e.getStatus());
		} catch (DataAccessException e) {
			return new ResponseEntity<>(MyJSON.message("Database error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
