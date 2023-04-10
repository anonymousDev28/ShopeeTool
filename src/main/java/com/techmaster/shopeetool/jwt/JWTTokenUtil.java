package com.techmaster.shopeetool.jwt;


import com.techmaster.shopeetool.model.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTTokenUtil{
    private static final Logger LOGGER = LoggerFactory.getLogger(JWTTokenUtil.class);
    private static final long EXPIRE_DURATION = 24*60*60*1000; //24h
    @Value("${app.jwt.secret}")
    private String secretKey;

    public String generateAccessToken(User user){
        return Jwts.builder()
                .setSubject(String.format("%s,%s",user.getId(),user.getEmail()))
                .claim("roles",user.getRoleSet().toString())
                .setIssuer("thomasdev")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS512,secretKey)
                .compact();

    }
    public Boolean validateAccessToken(String token){
        try{
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        }catch (ExpiredJwtException e){
            LOGGER.error("JWT expired",e);
        }catch (IllegalArgumentException e){
            LOGGER.error("Token is null, empty or has only whitespace",e);
        }catch (MalformedJwtException e){
            LOGGER.error("JWT is invalid",e);
        }catch (UnsupportedJwtException e){
            LOGGER.error("JWT is not supported",e);
        }catch (SignatureException e){
            LOGGER.error("Signature validation is failed",e);
        }
        return false;
    }
    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }
    public Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

}
