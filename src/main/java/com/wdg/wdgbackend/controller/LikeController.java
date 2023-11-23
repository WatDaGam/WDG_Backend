package com.wdg.wdgbackend.controller;

import com.wdg.wdgbackend.controller.service.LikeService;
import com.wdg.wdgbackend.controller.util.CustomException;
import com.wdg.wdgbackend.controller.util.MyJSON;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(
            summary = "좋아요 추가",
            description = "특정 스토리에 좋아요를 추가합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "좋아요 추가 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "SuccessResponse",
                                            value = "{\"message\": \"Like plus\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "잘못된 요청",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "BadRequestResponse",
                                            value = "{\"message\": \"Invalid story ID format\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "데이터베이스 에러",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "BadDatabase",
                                            value = "{\"message\": \"Error occurred while processing like\"}"
                                    )
                            )
                    )
            }
    )
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
