package com.wdg.wdgbackend.controller;

import com.wdg.wdgbackend.controller.service.TokenService;
import com.wdg.wdgbackend.controller.service.WithdrawalService;
import com.wdg.wdgbackend.controller.util.CustomException;
import com.wdg.wdgbackend.controller.util.MyJSON;
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
