package com.aistudy.backend.common;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtils {
    private static final String SECRET = "YourSuperSecretKeyForJWT_MustBeAtLeast256Bits!!";
    private static final long EXPIRATION = 7 * 24 * 60 * 60 * 1000; // 7天

    private SecretKey getKey(){
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }
    // 生成Token
    public String generateToken(Long userId, String username){
        return Jwts.builder()
                .subject(userId.toString())
                .claim("username", username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getKey())
                .compact();
    }
    //从Token中解析用户ID
    public Long getUserIdFromToken(String token){
        Claims claims = parseToken(token);
        return Long.parseLong(claims.getSubject());
    }
    //从Token中解析用户名
    public String getUsernameFromToken(String token){
        Claims claims = parseToken(token);
        return claims.get("username").toString();
    }
    // 验证Token
    public boolean validateToken(String token){
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    // 解析Token
    private Claims parseToken(String token){
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
