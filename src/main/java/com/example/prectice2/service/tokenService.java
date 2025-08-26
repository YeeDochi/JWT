package com.example.prectice2.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.prectice2.jwt.jwtUtil;
import com.example.prectice2.repository.userRepository;

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

    public String refreshAccessToken(String refreshToken) {
        if (jwtUtil.isExpired(refreshToken)) throw new RuntimeException("Refresh token expired");
        String username = jwtUtil.getUsername(refreshToken);
        String role = userRepository.findRoleByUsername(username);
        return jwtUtil.generateToken(username, role, accessTokenExpiration);
    }
}
