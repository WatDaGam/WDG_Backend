package com.wdg.wdgbackend.controller.service;

import com.wdg.wdgbackend.controller.util.CustomException;
import com.wdg.wdgbackend.model.entity.Story;
import com.wdg.wdgbackend.model.repository.StoryListRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class StoryListService {

	private static final int LIMIT = 20;

	private final StoryListRepository storyListRepository;

	@Autowired
	public StoryListService(StoryListRepository storyListRepository) {
		this.storyListRepository = storyListRepository;
	}

	@Transactional
	public JSONObject getNewListOfStory(String jsonData) {
		try {
			JSONObject jsonBody = new JSONObject(jsonData);
			return getStories(jsonBody);
		} catch (JSONException e) {
			log.error("JSON 파싱 오류", e);
			throw new CustomException("Invalid JSON format", e, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			log.error("Database 오류", e);
			throw new CustomException("Error occurred while retrieving story list", e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private JSONObject getStories(JSONObject jsonBody) {
		double lati = jsonBody.getDouble("lati");
		double longi = jsonBody.getDouble("longi");
		List<Story> storyList = storyListRepository.getStoriesByDistance(lati, longi, LIMIT);
		JSONObject response = new JSONObject();

		response.put("stories", new JSONArray(storyList));
		return response;
	}
}
