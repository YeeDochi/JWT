package com.example.prectice2.config;

import com.example.prectice2.jwt.jwtFilter;
import com.example.prectice2.jwt.jwtUtil;
import com.example.prectice2.jwt.loginFilter;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class securityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final jwtUtil jwtUtil;
    
    @Value("${spring.jwt.expiration}") private Long Exp;

    @Value("${spring.jwt.refresh-expiration}") private Long refreshExp;

    public securityConfig(AuthenticationConfiguration authenticationConfiguration,jwtUtil jwtUtil) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
    } // 메니저 생성에 있어서 필요한 변수를 주입받는다

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() { // 비밀번호 인코딩
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    } // 필터를 바꾸기 위한 메니저를 호출하는 메소드

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors((cors) -> cors.configurationSource(new CorsConfigurationSource() {
                @Override
                @Nullable
                public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                       
                        config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setMaxAge(3600L);

                        config.setExposedHeaders(Collections.singletonList("Authorization"));

                        return config;
                }
                }));

        http
                .csrf((auth)-> auth.disable());
        http
                .formLogin((auth)-> auth.disable());
        http
                .httpBasic((auth)-> auth.disable());
        http
                .authorizeHttpRequests((auth)-> auth
                        .requestMatchers("/login","/","/join","/refresh").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated()
                );

        http
                .addFilterBefore(new jwtFilter(jwtUtil),loginFilter.class);
        http
                .addFilterAt(new loginFilter(authenticationManager(authenticationConfiguration),jwtUtil,Exp,refreshExp), UsernamePasswordAuthenticationFilter.class);

        http
                .sessionManagement((session)->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

}
