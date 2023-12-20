package com.wdg.wdgbackend.controller.util;

import com.wdg.wdgbackend.controller.service.TokenService;
import com.wdg.wdgbackend.model.entity.User;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenService tokenService;

    @Autowired
    public JwtInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIp = getClientIp(request);
        String path = request.getServletPath();
        logRequestDetails(clientIp, path);

        if (!AllowedPaths.isAllowed(path)) {
            return makeResponse(response, HttpServletResponse.SC_NOT_FOUND, "Hello");
        }

        String token = extractToken(request.getHeader(AUTHORIZATION_HEADER));
        if (token == null) {
            return makeResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "No Authorization token provided");
        }

        return validateTokenAndRespond(token, response);
    }

    private String getClientIp(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For");
        return (clientIp != null) ? clientIp : request.getRemoteAddr();
    }

    private void logRequestDetails(String clientIp, String path) {
        if (log.isInfoEnabled()) {
            String formattedNow = LocalDateTime.now().format(FORMATTER);
            log.info("Time: {}, Client IP = {}, path = {}", formattedNow, clientIp, path);
        }
    }

    private String extractToken(String header) {
        return (header != null && header.startsWith(BEARER_PREFIX)) ? header.substring(BEARER_PREFIX.length()) : null;
    }

    private boolean validateTokenAndRespond(String token, HttpServletResponse response) throws IOException {
        try {
            Optional<User> user = tokenService.validateToken(token);
            return user.isPresent() || makeResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized - Invalid Token");
        } catch (JwtException | IllegalArgumentException e) {
            return makeResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid token format");
        } catch (Exception e) {
            return makeResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    private static boolean makeResponse(HttpServletResponse response, int statusCode, String msg) throws IOException {
        response.setStatus(statusCode);
        response.getWriter().write("{\"error\":\"" + msg + "\"}");
        return false;
    }
}
