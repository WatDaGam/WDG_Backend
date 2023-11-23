package com.wdg.wdgbackend.controller;

import com.wdg.wdgbackend.controller.service.LoginService;
import com.wdg.wdgbackend.controller.service.NicknameService;
import com.wdg.wdgbackend.controller.service.TokenService;
import com.wdg.wdgbackend.controller.util.CustomException;
import com.wdg.wdgbackend.controller.util.MyJSON;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
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
@RequestMapping("/nickname")
public class NicknameController {

	private final NicknameService nicknameService;
	private final LoginService loginService;
	private final TokenService tokenService;

	@Autowired
	public NicknameController(NicknameService nicknameService, LoginService loginService, TokenService tokenService) {
		this.nicknameService = nicknameService;
		this.loginService = loginService;
		this.tokenService = tokenService;
	}


	@PostMapping("/check")
	@Operation(
			summary = "닉네임 중복 확인",
			description = "제공된 닉네임이 중복되는지 확인합니다.",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					required = true,
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(
									type = "string",
									example = "desiredNickname"
							)
					)
			),
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "닉네임 중복 여부 확인",
							content = @Content(
									mediaType = "application/json",
									examples = {
											@ExampleObject(name = "NicknameNotDuplicated", value = "{\"message\": \"nickname not duplicated\"}"),
											@ExampleObject(name = "NicknameDuplicated", value = "{\"message\": \"nickname duplicated\"}")
									}
							)
					),
					@ApiResponse(
							responseCode = "400",
							description = "잘못된 요청",
							content = @Content(
									mediaType = "application/json",
									examples = @ExampleObject(name = "BadRequest", value = "{\"message\": \"Invalid nickname\"}")
							)
					),
					@ApiResponse(
							responseCode = "500",
							description = "서버 내부 오류",
							content = @Content(
									mediaType = "application/json",
									examples = @ExampleObject(name = "ServerError", value = "{\"message\": \"Database error\"}")
							)
					)
			}
	)
	public ResponseEntity<String> checkNickname(@RequestBody String nickname) {
		if (nickname == null || nickname.trim().isEmpty()) {
			return new ResponseEntity<>(MyJSON.message("Invalid nickname"), HttpStatus.BAD_REQUEST);
		}

		try {
			if (nicknameService.isNicknameDuplicated(nickname)) {
				return new ResponseEntity<>(MyJSON.message("nickname duplicated"), HttpStatus.OK);
			}
			return new ResponseEntity<>(MyJSON.message("nickname not duplicated"), HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<>(MyJSON.message("Database error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/set")
	@Operation(
			summary = "닉네임 설정",
			description = "사용자의 닉네임을 설정합니다.",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					required = true,
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(
									type = "string",
									example = "newNickname"
							)
					)
			),
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "닉네임 설정 성공",
							content = @Content(
									mediaType = "application/json",
									examples = @ExampleObject(name = "SuccessResponse", value = "{\"Authorization\": \"Bearer token\", \"Refresh-Token\": \"refresh token\"}")
							)
					),
					@ApiResponse(
							responseCode = "400",
							description = "잘못된 요청",
							content = @Content(
									mediaType = "application/json",
									examples = {
											@ExampleObject(name = "InvalidAuthorizationHeader", value = "{\"message\": \"Invalid Authorization Header\"}"),
											@ExampleObject(name = "InvalidNickname", value = "{\"message\": \"Invalid nickname\"}")
									}
							)
					),
					@ApiResponse(
							responseCode = "500",
							description = "서버 내부 오류",
							content = @Content(
									mediaType = "application/json",
									examples = @ExampleObject(name = "ServerError", value = "{\"message\": \"Database error\"}")
							)
					)
			}
	)
	public ResponseEntity<String> setNickname(@RequestBody String nickname, @RequestHeader("Authorization") String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return new ResponseEntity<>(MyJSON.message("Invalid Authorization Header"), HttpStatus.BAD_REQUEST);
		}
		if (nickname == null || nickname.trim().isEmpty()) {
			return new ResponseEntity<>(MyJSON.message("Invalid nickname"), HttpStatus.BAD_REQUEST);
		}

		try {
			Long userId = tokenService.getIdFromAccessToken(authorizationHeader);
			Long snsId = tokenService.getSnsIdFromAccessToken(authorizationHeader);

			if (nicknameService.isNicknameDuplicated(nickname)) {
				return new ResponseEntity<>(MyJSON.message("nickname duplicated"), HttpStatus.OK);
			}
			nicknameService.setNickname(userId, nickname);
			HttpHeaders responseHeaders = loginService.getHttpHeaders(snsId);
			return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
		} catch (CustomException e) {
			return new ResponseEntity<>(MyJSON.message(e.getMessage()), e.getStatus());
		} catch (DataAccessException e) {
			return new ResponseEntity<>(MyJSON.message("Database error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
