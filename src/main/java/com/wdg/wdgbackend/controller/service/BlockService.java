package com.wdg.wdgbackend.controller.service;

import com.wdg.wdgbackend.controller.util.CustomException;
import com.wdg.wdgbackend.model.entity.Block;
import com.wdg.wdgbackend.model.entity.User;
import com.wdg.wdgbackend.model.repository.BlockRepository;
import com.wdg.wdgbackend.model.repository.UserRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlockService {

	private final BlockRepository blockRepository;
	private final TokenService tokenService;
	private final StoryUserLikeService storyUserLikeService;
	private final UserRepository userRepository;

	@Autowired
	public BlockService(BlockRepository blockRepository, TokenService tokenService, StoryUserLikeService storyUserLikeService, UserRepository userRepository) {
		this.blockRepository = blockRepository;
		this.tokenService = tokenService;
		this.storyUserLikeService = storyUserLikeService;
		this.userRepository = userRepository;
	}

	public void blockUser(String authorizationHeader, String writerId) {
		long userId = tokenService.getIdFromAccessToken(authorizationHeader);
		long writerIdL = Long.parseLong(writerId);
		Optional<User> blockedUser = userRepository.findUserById(writerIdL);
		if (blockedUser.isEmpty()) return;
		String writerNickname = blockedUser.get().getNickname();

		try {
			blockRepository.insertBlock(userId, writerIdL, writerNickname);
		} catch (DataAccessException e) {
			throw new CustomException("Data access error: " + e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public JSONObject getBlockUserList(String authorizationHeader) {
		long userId = tokenService.getIdFromAccessToken(authorizationHeader);
		try {
			List<Block> blockedUsers = blockRepository.getBlockedUsers(userId);
			JSONObject response = new JSONObject();
			response.put("blockedUsers", new JSONArray(blockedUsers));
			return response;
		} catch (JSONException e) {
			throw new CustomException("Invalid JSON format", e, HttpStatus.BAD_REQUEST);
		} catch (DataAccessException e) {
			throw new CustomException("Data access error: " + e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public void unblockUser(String authorizationHeader, String id) {
		long userId = tokenService.getIdFromAccessToken(authorizationHeader);
		long blockedUserId = Long.parseLong(id);

		try {
			blockRepository.deleteUserBlocks(userId, blockedUserId);
		} catch (DataAccessException e) {
			throw new CustomException("Data access error: " + e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
