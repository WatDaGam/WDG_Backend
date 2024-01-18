package com.wdg.wdgbackend.model.mapper;

import com.wdg.wdgbackend.model.entity.Block;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BlockMapper {
	@Insert("INSERT INTO blocks (userId, writerId, writerNickname) VALUES (#{userId}, #{writerId}, #{writerNickname})")
	void insertBlock(long userId, long writerId, String writerNickname);

	@Select("SELECT writerId, writerNickname from blocks WHERE userId = ${userId}")
	List<Block> getBlockedUsers(long userId);

	@Delete("DELETE FROM blocks WHERE userId = #{userId} AND writerId = #{blockedUserId}")
	void deleteUserBlocks(long userId, long blockedUserId);

	@Delete("DELETE FROM blocks WHERE userId = #{userId}")
	void deleteUserBlocksWithdrawal(long userId);
}
