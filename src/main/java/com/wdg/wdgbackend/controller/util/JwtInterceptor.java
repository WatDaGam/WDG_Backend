package com.wdg.wdgbackend.controller.util;

import com.wdg.wdgbackend.controller.service.JwtService;
import com.wdg.wdgbackend.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;

    @Autowired
    public JwtInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader("Authorization");
        String token = null;

        if(header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
        }
        Optional<User> user = jwtService.validateToken(token);

        if (user.isPresent()) {
            return true;
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Unauthorized - Invalid Token\"}");
            return false;
        }
    }
}
