package com.wdg.wdgbackend.controller;

import com.wdg.wdgbackend.controller.service.TokenService;
import com.wdg.wdgbackend.controller.service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
		Long idFromAccessToken = tokenService.getIdFromAccessToken(authorizationHeader);
		withdrawalService.deleteUser(idFromAccessToken);
		return new ResponseEntity<>("Deleted", HttpStatus.OK);
	}
}
