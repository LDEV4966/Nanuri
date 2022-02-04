package com.example.nanuri.jwt;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

import java.util.Date;
import java.util.Random;

@Component
public class JwtTokenProvider {
    private static final String SECRET_KEY = "dsjakjsjndjakndnaks";
    private static long accessTokenValidityInMilliseconds = 1*1000*60;
    private static long refreshTokenValidityInMilliseconds = 2*1000*60;

    public String createAccessToken(String payload){
        return createToken(payload, accessTokenValidityInMilliseconds);
    }

    public String createRefreshToken(){
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        String generatedString = new String(array, StandardCharsets.UTF_8);
        return  createToken(generatedString,refreshTokenValidityInMilliseconds);
    }

    public String createToken(String payload, long expireLength){

       Claims claims = Jwts.claims().setSubject(payload); // token에 실제로 들어갈 값
       Date now = new Date();
       Date validity = new Date(now.getTime()+expireLength);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY)
                .compact();
    }

    public String getPayload(String token){
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e){
            return e.getClaims().getSubject();
        } catch (JwtException e) {
            throw  new RuntimeException("유효하지 않는 토큰입니다.");
        }
    }

    public boolean validateToken(String token){
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

}
