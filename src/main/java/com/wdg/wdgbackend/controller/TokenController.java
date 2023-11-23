package com.wdg.wdgbackend.controller;

import com.wdg.wdgbackend.controller.service.TokenService;
import com.wdg.wdgbackend.controller.util.MyJSON;
import com.wdg.wdgbackend.model.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
public class TokenController {

    private static final long ACCESS_TOKEN_EXPIRY = 1000L * 60 * 10; // 10분

    private final TokenService tokenService;

    @Autowired
    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("/refreshtoken")
    @Operation(
            summary = "리프레시 토큰으로 엑세스 토큰 갱신",
            description = "제공된 리프레시 토큰을 검증하고 새로운 엑세스 토큰을 발급합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "엑세스 토큰 갱신 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "SuccessResponse",
                                            value = "{\"Authorization\": \"Bearer new_access_token\", \"Access-Expiration-Time\": \"expiration_time\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "리프레시 토큰 없음",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "NoRefreshToken",
                                            value = "{\"message\": \"Refresh Token is required\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "리프레시 토큰 무효 또는 만료",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "InvalidOrExpiredRefreshToken",
                                            value = "{\"message\": \"Refresh Token is invalid or expired\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "ServerError",
                                            value = "{\"message\": \"Error occurred while validating token\"}"
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<?> refreshToken(@RequestHeader("Refresh-Token") String refreshToken) {
        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            return new ResponseEntity<>(MyJSON.message("Refresh Token is required"), HttpStatus.BAD_REQUEST);
        }

        try {
            Optional<User> requestUser = tokenService.validateToken(refreshToken);
            if (requestUser.isPresent()) {
                HttpHeaders responseHeaders = new HttpHeaders();
                long systemTime = System.currentTimeMillis() + ACCESS_TOKEN_EXPIRY;
                responseHeaders.add("Authorization", "Bearer " + tokenService.generateAccessToken(requestUser.get(), systemTime));
                responseHeaders.add("Access-Expiration-Time", String.valueOf(systemTime));
                return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(MyJSON.message("Refresh Token is invalid or expired"), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(MyJSON.message("Error occurred while validating token"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
