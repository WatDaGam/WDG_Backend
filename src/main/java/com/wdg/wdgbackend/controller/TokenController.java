package com.wdg.wdgbackend.controller;

import com.wdg.wdgbackend.controller.service.JwtService;
import com.wdg.wdgbackend.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class TokenController {

    private JwtService jwtService;

    @Autowired
    public TokenController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@RequestHeader("Refresh-Token") String refreshToken) {
        Optional<User> requestUser = jwtService.validateToken(refreshToken);
        if (requestUser.isPresent()) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("Authorization", "Bearer " + jwtService.generateAccessToken(requestUser.get()));
            return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Refresh token is invalid or expired", HttpStatus.UNAUTHORIZED);
        }
    }
}