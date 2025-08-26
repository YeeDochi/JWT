package com.example.prectice2.jwt;

import com.example.prectice2.service.customUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.management.remote.JMXAuthenticator;
import java.util.Collection;
import java.util.Iterator;

public class loginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final jwtUtil jwtUtil;
    private final Long Exp;
    private final Long refreshExp;

    public loginFilter(AuthenticationManager authenticationManager, jwtUtil jwtUtil, Long Exp, Long refreshExp) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.Exp = Exp;
        this.refreshExp = refreshExp;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        //System.out.println("username: " + username+"\n password: "+password);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password,null);
        return this.authenticationManager.authenticate(token);

    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) {
        System.out.println("success");
        customUserDetails customUserDetails = (customUserDetails) auth.getPrincipal();
        String username = customUserDetails.getUsername();
        Collection<? extends GrantedAuthority> authorities = customUserDetails.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority Auth = iterator.next();
        String role = Auth.getAuthority();
        
        String token = jwtUtil.generateToken(username,role, Exp);
        String refreshToken = jwtUtil.generateRefreshToken(username, refreshExp);
        response.addHeader("Authorization", "Bearer " + token);
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7일 등 만료시간 설정

        response.addCookie(refreshCookie);
    }
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        System.out.println("unsuccessful");

        response.setStatus(401);
    }
}
