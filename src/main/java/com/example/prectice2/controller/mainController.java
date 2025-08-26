package com.example.prectice2.controller;
import java.util.Collection;
import java.util.Iterator;

import com.example.prectice2.service.tokenService;
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
            String newAccessToken = tokenService.refreshAccessToken(refreshToken);
            response.setHeader("Authorization", "Bearer " + newAccessToken);
            return "successfully refreshed token";
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return e.getMessage();
        }
    }   
}
