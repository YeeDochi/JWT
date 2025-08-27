package com.example.prectice2.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.prectice2.entity.userEntity;
import com.example.prectice2.repository.userRepository;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class jwtUtil {
    private SecretKey jwtSecret;
    public jwtUtil(@Value("${spring.jwt.secret}") String jwtSecret) {
        this.jwtSecret = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getUsername(String token) {
        return Jwts.parser().verifyWith(jwtSecret).build().parseSignedClaims(token).getPayload().get("username",String.class);
    }
    public String getRole(String token) {
        return Jwts.parser().verifyWith(jwtSecret).build().parseSignedClaims(token).getPayload().get("role",String.class);
    }
    public boolean isExpired(String token) {
        return Jwts.parser().verifyWith(jwtSecret).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }
    public String generateToken(String username, String role, Long expiration) {

        return Jwts.builder()
                .claim("username",username)
                .claim("role",role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(jwtSecret, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String username, Long expiration) {

        return Jwts.builder()
                .claim("username",username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(jwtSecret, SignatureAlgorithm.HS256)
                .compact();
    }

    // public String refreshAccessToken(String refreshToken, userRepository userRepository, Long expiration) {
    //  if (isExpired(refreshToken)) {
    //     throw new RuntimeException("Refresh token expired");
    //     }
    //     String username = getUsername(refreshToken);
    //     userEntity user = userRepository.findByUsername(username);
    //     String role = user.getRole();
    //     return generateToken(username, role, expiration);
    // }
}
