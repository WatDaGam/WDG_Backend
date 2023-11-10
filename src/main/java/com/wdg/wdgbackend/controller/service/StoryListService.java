package com.wdg.wdgbackend.controller.service;

import com.wdg.wdgbackend.model.entity.Story;
import com.wdg.wdgbackend.model.repository.StoryListRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
		JSONObject jsonBody = new JSONObject(jsonData);
		return getStories(jsonBody);
	}

	private JSONObject getStories(JSONObject jsonBody) {
		double lati = jsonBody.getDouble("lati");
		double longi = jsonBody.getDouble("longi");
		List<Story> storyList = storyListRepository.getStoriesByDistance(lati, longi, LIMIT);

		return makeResponseJSON(storyList);
	}

	private JSONObject makeResponseJSON(List<Story> storyList) {
		JSONObject response = new JSONObject();
		response.put("stories", new JSONArray(storyList));

		return response;
	}
}
