package com.wdg.wdgbackend.model.repository;

import com.wdg.wdgbackend.model.entity.Story;

import java.sql.Timestamp;
import java.util.List;

public interface StoryListRepository {

	List<Story> getStoriesByDistance(long userId, double lati, double longi, int initialLimit, int finalLimit);
	void increaseRenewNum(long userId);
}
