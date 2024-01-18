package com.wdg.wdgbackend.model.repository;

import com.wdg.wdgbackend.model.entity.Block;

import java.util.List;

public interface BlockRepository {
	void insertBlock(long userId, long writerId, String writerNickname);
	List<Block> getBlockedUsers(long userId);
	void deleteUserBlocks(long userId, long blockedUserId);
	void deleteUserBlocksWithdrawal(long userId);
}
