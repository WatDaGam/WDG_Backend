package com.wdg.wdgbackend.controller;

import com.wdg.wdgbackend.controller.service.TokenService;
import com.wdg.wdgbackend.controller.service.WithdrawalService;
import com.wdg.wdgbackend.controller.util.CustomException;
import com.wdg.wdgbackend.controller.util.MyJSON;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/withdrawal")
public class WithdrawalController {

	private final WithdrawalService withdrawalService;
	private final TokenService tokenService;

	@Autowired
	public WithdrawalController(WithdrawalService withdrawalService, TokenService tokenService) {
		this.withdrawalService = withdrawalService;
		this.tokenService = tokenService;
	}

	@DeleteMapping
	@Operation(
			summary = "회원 탈퇴",
			description = "사용자가 회원 탈퇴를 진행합니다.",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "회원 탈퇴 성공",
							content = @Content(
									mediaType = "text/plain",
									examples = @ExampleObject(
											name = "SuccessResponse",
											value = "Deleted"
									)
							)
					),
					@ApiResponse(
							responseCode = "400",
							description = "잘못된 인증 헤더",
							content = @Content(
									mediaType = "application/json",
									examples = @ExampleObject(
											name = "InvalidHeader",
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
											name = "ServerError",
											value = "{\"message\": \"Database error\"}"
									)
							)
					)
			}
	)
	public ResponseEntity<String> getout(@RequestHeader("Authorization") String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return new ResponseEntity<>(MyJSON.message("Invalid Authorization Header"), HttpStatus.BAD_REQUEST);
		}
		try {
			Long userId = tokenService.getIdFromAccessToken(authorizationHeader);

			withdrawalService.deleteUser(userId);
			return new ResponseEntity<>("Deleted", HttpStatus.OK);
		} catch (CustomException e) {
			return new ResponseEntity<>(MyJSON.message(e.getMessage()), e.getStatus());
		}
	}
}
