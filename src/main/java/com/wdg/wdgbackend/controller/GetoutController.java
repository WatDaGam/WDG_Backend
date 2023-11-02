package com.wdg.wdgbackend.controller;

import com.wdg.wdgbackend.controller.service.GetoutService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/withdrawl")
public class GetoutController {

	private final GetoutService getoutService;

	@Autowired
	public GetoutController(GetoutService getoutService) {
		this.getoutService = getoutService;
	}

	@DeleteMapping
	public ResponseEntity<String> getout(@RequestParam("nickname") String nickname) {
		getoutService.deleteUser(nickname);
		return new ResponseEntity<>("Deleted", HttpStatus.OK);
	}
}
