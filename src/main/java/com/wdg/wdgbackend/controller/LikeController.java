package com.wdg.wdgbackend.controller;

import com.wdg.wdgbackend.controller.service.LikeService;
import com.wdg.wdgbackend.controller.util.CustomException;
import com.wdg.wdgbackend.controller.util.MyJSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
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
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>(MyJSON.message("Invalid Authorization Header"), HttpStatus.BAD_REQUEST);
        }
        if (storyId == null || storyId.trim().isEmpty()) {
            return new ResponseEntity<>(MyJSON.message("Invalid storyId"), HttpStatus.BAD_REQUEST);
        }
        try {
            likeService.likePlus(authorizationHeader, storyId);
        } catch (CustomException e) {
            return new ResponseEntity<>(MyJSON.message(e.getMessage()), e.getStatus());
        }
        return new ResponseEntity<>(MyJSON.message("Like plus"), HttpStatus.OK);
    }
}
