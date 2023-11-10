package com.wdg.wdgbackend.controller;

import com.wdg.wdgbackend.controller.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
public class LikeController {

    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/plus")
    public ResponseEntity<String> likePlus(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("storyId") String storyId) {
        likeService.likePlus(authorizationHeader, storyId);
        return new ResponseEntity<>("Like Plus", HttpStatus.OK);
    }
}
