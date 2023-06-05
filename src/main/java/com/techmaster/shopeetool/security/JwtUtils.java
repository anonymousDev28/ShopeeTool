package com.techmaster.shopeetool.security;


import com.techmaster.shopeetool.exception.TokenRefreshException;
import com.techmaster.shopeetool.model.RefreshToken;
import com.techmaster.shopeetool.repository.RefreshTokenRepository;
import com.techmaster.shopeetool.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class JwtUtils {
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;
    @Value("${shopeetool.app.jwtExpirationMs}")
    private int jwtExpirationMs;
    @Value("${shopeetool.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;
    // TODO: Tạo token từ thông tin của user
    public String generateToken(UserDetails userDetails) {
//        Map<String, Object> extraClaims = new HashMap<>();
//        extraClaims.put("authorities", userDetails.getAuthorities());
//        String token = "";
//        try {
//
//            token = Jwts
//                    .builder()
//                    .setClaims(extraClaims)
//                    .setSubject(userDetails.getUsername())
//                    .setIssuedAt(new Date(System.currentTimeMillis()))
//                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
//                    .signWith(getSignInKey())
//                    .compact();
//
//            //OK, we can trust this JWT
//
//        } catch (JwtException e) {
//
//            //don't trust the JWT!
//        }
//        return token;
        return builtToken(userDetails,jwtExpiration);
    }
    public String generateRefreshToken(UserDetails userDetails) {
        return builtToken(userDetails,refreshTokenExpiration);
    }
    public RefreshToken createRefreshToken(UserDetails userDetails) {
        // modify
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userRepository.findByEmail(userDetails.getUsername()).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(builtToken(userDetails,refreshTokenExpiration));
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    private String builtToken(UserDetails userDetails,long expiration){
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("authorities", userDetails.getAuthorities());
        String token = "";
        try {

            token = Jwts
                    .builder()
                    .setClaims(extraClaims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(getSignInKey())
                    .compact();

            //OK, we can trust this JWT

        } catch (JwtException e) {

            //don't trust the JWT!
        }
        return token;
    }
    // TODO: Lấy danh sách claims từ trong token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build().parseClaimsJws(token).getBody();
    }

    // TODO: Lấy username của user từ trong token
    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    // TODO: Lấy ngày hết hạn của token
    private Date extractExpiration(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getExpiration();
    }

    // TODO: Kiểm tra xem token đã hết hạn hay chưa
    private boolean isTokenExpired(String token) {
        return new Date().after(extractExpiration(token));
    }

    // TODO: Kiểm tra token có chuẩn không (trùng username + chưa hết hạn)
    public boolean isTokenValid(String token, UserDetails userDetails) {
        return userDetails.getUsername().equals(extractUsername(token)) && !isTokenExpired(token);
    }

    // Build secret key jwt
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
