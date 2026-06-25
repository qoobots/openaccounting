package com.qoobot.openaccounting.system.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * JWT Token工具类
 *
 * @author openaccounting
 */
@Component
public class JwtTokenUtil {

    /**
     * Token前缀
     */
    private static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 密钥
     */
    @Value("${jwt.secret:openaccounting-secret-key-2024-for-jwt-token-generation}")
    private String secret;

    /**
     * Access Token过期时间（毫秒） 默认2小时
     */
    @Value("${jwt.expiration:7200000}")
    private Long expiration;

    /**
     * Refresh Token过期时间（毫秒） 默认7天
     */
    @Value("${jwt.refresh-expiration:604800000}")
    private Long refreshExpiration;

    /**
     * 生成密钥
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 从Token中获取Claims
     *
     * @param token Token
     * @return Claims
     */
    private Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 生成Access Token
     *
     * @param userId   用户ID
     * @param username 用户名
     * @param claims   额外信息
     * @return Token
     */
    public String generateAccessToken(Long userId, String username, Map<String, Object> claims) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        var builder = Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .claim("userId", userId);

        if (claims != null) {
            claims.forEach(builder::claim);
        }

        return builder.compact();
    }

    /**
     * 生成Refresh Token
     *
     * @param userId   用户ID
     * @param username 用户名
     * @return Refresh Token
     */
    public String generateRefreshToken(Long userId, String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshExpiration);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .claim("userId", userId)
                .claim("type", "refresh")
                .compact();
    }

    /**
     * 从Token中获取用户名
     *
     * @param token Token
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? claims.getSubject() : null;
    }

    /**
     * 从Token中获取用户ID
     *
     * @param token Token
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? claims.get("userId", Long.class) : null;
    }

    /**
     * 检查是否为Refresh Token
     *
     * @param token Token
     * @return 是否为Refresh Token
     */
    public boolean isRefreshToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            String type = claims.get("type", String.class);
            return "refresh".equals(type);
        }
        return false;
    }

    /**
     * 获取Refresh Token过期时间
     *
     * @return 过期时间（秒）
     */
    public Long getRefreshExpiration() {
        return refreshExpiration / 1000;
    }

    /**
     * 判断Token是否过期
     *
     * @param token Token
     * @return 是否过期
     */
    public Boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 验证Token
     *
     * @param token Token
     * @return 是否有效
     */
    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token) && getClaimsFromToken(token) != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取Token过期时间
     *
     * @return 过期时间（秒）
     */
    public Long getExpiration() {
        return expiration / 1000;
    }

    /**
     * 从请求头中获取Token
     *
     * @param authHeader Authorization header
     * @return Token
     */
    public String getTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
            return authHeader.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
