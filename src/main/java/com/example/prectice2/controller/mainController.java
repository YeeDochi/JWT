package com.example.prectice2.controller;
import java.util.Collection;
import java.util.Iterator;

import com.example.prectice2.service.tokenService;
import com.example.prectice2.DTO.loginDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;


@Controller
@ResponseBody
public class mainController {
 
    private final tokenService tokenService;

   
    public mainController(tokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("/")
    public String mainP(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority Auth = iterator.next();
        String role = Auth.getAuthority();
        return "main Controller " + username+" "+role;
    }

    @PostMapping("/refresh")
    public String postMethodName(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = null;
        if(request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("refreshToken")) {
                    refreshToken = cookie.getValue();
                }
            }
        }
        try {
            loginDto tokens = tokenService.refreshAccessToken(refreshToken, response);
            Cookie refreshCookie = new Cookie("refreshToken", tokens.refreshToken());
            refreshCookie.setHttpOnly(true);
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7일 등 만료시간 설정
            response.addCookie(refreshCookie);
            response.setHeader("Authorization", "Bearer " + tokens.accessToken());
            return "successfully refreshed token";
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return e.getMessage();
        }
    }   
}
