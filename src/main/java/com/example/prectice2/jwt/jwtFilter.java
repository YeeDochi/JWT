package com.example.prectice2.jwt;

import com.example.prectice2.entity.userEntity;
import com.example.prectice2.service.customUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class jwtFilter extends OncePerRequestFilter {

    private final jwtUtil jwtUtil;
    public jwtFilter(jwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            System.out.println("Token is notbailed");
            filterChain.doFilter(request,response);
            return;
        }
        System.out.println("autherization start");
        String token = authorization.split(" ")[1];
        boolean expire = jwtUtil.isExpired(token);

        System.out.println("token expire:"+expire);
        if(expire){
            System.out.println("Token is expired");
            filterChain.doFilter(request,response);
            return;
        }
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        userEntity userEntity = new  userEntity();
        userEntity.setUsername(username);
        userEntity.setRole(role);
        userEntity.setPassword(null);
        customUserDetails customUserDetails = new customUserDetails(userEntity);
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails,null,customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request,response);
    }
}
