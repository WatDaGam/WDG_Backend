package com.wdg.wdgbackend.controller.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.wdg.wdgbackend.model.entity.Story;
import com.wdg.wdgbackend.model.repository.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoryService {

	private final StoryRepository storyRepository;

	@Autowired
	public StoryService(StoryRepository storyRepository) {
		this.storyRepository = storyRepository;
	}

	public void insertNewStory(Long id, JsonNode rootNode) {
		String story = rootNode.get("story").asText();
		double lati = rootNode.get("lati").asDouble();
		double longi = rootNode.get("longi").asDouble();

		storyRepository.insertStory(new Story(0L, id, story, 0, lati, longi));
	}
}
