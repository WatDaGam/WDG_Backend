package com.wdg.wdgbackend.model.repository;

import com.wdg.wdgbackend.model.entity.Block;
import com.wdg.wdgbackend.model.mapper.BlockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BlockRepositoryImpl implements BlockRepository {

	private final BlockMapper blockMapper;

	@Autowired
	public BlockRepositoryImpl(BlockMapper blockMapper) {
		this.blockMapper = blockMapper;
	}

	@Override
	public void insertBlock(long userId, long writerId, String writerNickname) {
		blockMapper.insertBlock(userId, writerId, writerNickname);
	}

	@Override
	public List<Block> getBlockedUsers(long userId) {
		return blockMapper.getBlockedUsers(userId);
	}

	@Override
	public void deleteUserBlocks(long userId, long blockedUserId) {
		blockMapper.deleteUserBlocks(userId, blockedUserId);
	}

	@Override
	public void deleteUserBlocksWithdrawal(long userId) {
		blockMapper.deleteUserBlocksWithdrawal(userId);
	}
}
