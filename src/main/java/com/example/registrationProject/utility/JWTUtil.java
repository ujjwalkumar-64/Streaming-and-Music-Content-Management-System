package com.example.registrationProject.utility;

import com.example.registrationProject.entity.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JWTUtil {
    private static final String JWT_SECRET="4444445hjhdsabdsyu7we8726836wb88shwgu387737737eu";
    private static final Key key= Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));



    public String generateToken(String email, User user, long expiryMinutes){
        return Jwts.builder()
                .setSubject(email)
                .claim("role",user.getRole().getRole())
                .claim("name", user.getFullName())
                .claim("id", user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+expiryMinutes*60*1000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

    }

    public    String validateAndExtractUsername(String token){
        try{
            return Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }
        catch (JwtException e){
            return null;
        }
    }
}
