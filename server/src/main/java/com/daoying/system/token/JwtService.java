package com.daoying.system.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT(JSON Web Token)方法
 * @author daoying
 */

@Service
public class JwtService {
    /**
     * 加密盐值
     */
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    /**
     * Token失效时间
     */
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    /**
     * Token刷新时间
     */
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    /**
     * 从Token中获取Username
     * @param token Token
     * @return String
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 从Token中回去数据,根据传入不同的Function返回不同的数据
     * eg: String extractUsername(String token)
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 生成Token无额外信息
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * 生成Token,有额外信息
     * @param extraClaims 额外的数据
     * @param userDetails 用户信息
     * @return String
     */
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    public String generateRefreshToken(
            UserDetails userDetails
    ) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims) //body
                .setSubject(userDetails.getUsername()) //主题数据
                .setIssuedAt(new Date(System.currentTimeMillis())) //设置发布时间
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) //设置过期时间
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) //设置摘要算法
                .compact();
    }

    /**
     * 验证Token是否有效
     * @param token Token
     * @param userDetails 用户信息
     * @return boolean
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * 判断Token是否过期
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * 从Token中获取失效时间
     */
    private Date extractExpiration(String token) {
        //通用方法,传入一个Function,返回一个T
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * 从Token中获取所有数据
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 获取签名Key
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
