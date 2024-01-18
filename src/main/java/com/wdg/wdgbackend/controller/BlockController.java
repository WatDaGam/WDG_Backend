package com.wdg.wdgbackend.controller;

import com.wdg.wdgbackend.controller.service.BlockService;
import com.wdg.wdgbackend.controller.util.CustomException;
import com.wdg.wdgbackend.controller.util.MyJSON;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/block")
public class BlockController {

	private final BlockService blockService;

	@Autowired
	public BlockController(BlockService blockService) {
		this.blockService = blockService;
	}

	@PostMapping
	@Operation(
			summary = "Block a User",
			description = "Block a user based on the story ID.",
			responses = {
					@ApiResponse(responseCode = "200", description = "User successfully blocked"),
					@ApiResponse(responseCode = "400", description = "Bad Request"),
					@ApiResponse(responseCode = "500", description = "Internal Server Error")
			}
	)
	public ResponseEntity<String> block(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("writerId") String writerId) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return new ResponseEntity<>(MyJSON.message("Invalid Authorization Header"), HttpStatus.BAD_REQUEST);
		}
		if (writerId == null || writerId.trim().isEmpty()) {
			return new ResponseEntity<>(MyJSON.message("Invalid storyId"), HttpStatus.BAD_REQUEST);
		}

		try {
			blockService.blockUser(authorizationHeader, writerId);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (CustomException e) {
			return new ResponseEntity<>(e.getStatus());
		}
	}

	@PostMapping("/remove")
	@Operation(
			summary = "Unblock a User",
			description = "Unblock a user based on the user ID.",
			responses = {
					@ApiResponse(responseCode = "200", description = "User successfully unblocked"),
					@ApiResponse(responseCode = "400", description = "Bad Request"),
					@ApiResponse(responseCode = "500", description = "Internal Server Error")
			}
	)
	public ResponseEntity<String> remove(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("blockedUserId") String blockedUserId) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return new ResponseEntity<>(MyJSON.message("Invalid Authorization Header"), HttpStatus.BAD_REQUEST);
		}
		if (blockedUserId == null || blockedUserId.trim().isEmpty()) {
			return new ResponseEntity<>(MyJSON.message("Invalid storyId"), HttpStatus.BAD_REQUEST);
		}

		try {
			blockService.unblockUser(authorizationHeader, blockedUserId);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (CustomException e) {
			return new ResponseEntity<>(e.getStatus());
		}
	}

	@GetMapping("/list")
	@Operation(
			summary = "List Blocked Users",
			description = "Retrieve a list of blocked users.",
			responses = {
					@ApiResponse(responseCode = "200", description = "List retrieved successfully"),
					@ApiResponse(responseCode = "400", description = "Bad Request"),
					@ApiResponse(responseCode = "500", description = "Internal Server Error")
			}
	)
	public ResponseEntity<String> blockList(@RequestHeader("Authorization") String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return new ResponseEntity<>(MyJSON.message("Invalid Authorization Header"), HttpStatus.BAD_REQUEST);
		}

		try {
			JSONObject blockUserList = blockService.getBlockUserList(authorizationHeader);
			return new ResponseEntity<>(blockUserList.toString(), HttpStatus.OK);
		} catch (CustomException e) {
			return new ResponseEntity<>(e.getStatus());
		}
	}
}
