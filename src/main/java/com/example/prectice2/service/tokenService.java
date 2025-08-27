package com.example.prectice2.service;

import org.springframework.beans.factory.annotation.Value;
// import org.springframework.boot.web.server.Cookie; // Remove this import
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;

import com.example.prectice2.DTO.loginDto;
import com.example.prectice2.entity.userEntity;
import com.example.prectice2.jwt.jwtUtil;
import com.example.prectice2.repository.userRepository;

import jakarta.servlet.http.HttpServletMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class tokenService {
    @Value("${spring.jwt.expiration}")
    private Long accessTokenExpiration;

    private final jwtUtil jwtUtil;
    private final userRepository userRepository;

    public tokenService(jwtUtil jwtUtil, userRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    public loginDto refreshAccessToken(String refreshToken,HttpServletResponse response) {
        if (jwtUtil.isExpired(refreshToken)) throw new RuntimeException("Refresh token expired");
           
        Cookie cookie = new Cookie("refreshToken", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); 
        response.addCookie(cookie);
           
        
        String username = jwtUtil.getUsername(refreshToken);
        userEntity user = userRepository.findByUsername(username);
        String role = user.getRole();
        String accessToken = jwtUtil.generateToken(username, role, accessTokenExpiration);
        return new loginDto(accessToken, refreshToken);
    }
}
