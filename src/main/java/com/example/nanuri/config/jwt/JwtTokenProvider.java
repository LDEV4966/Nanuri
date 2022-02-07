package com.example.nanuri.config.jwt;

import com.example.nanuri.handler.exception.AuthenticationNullPointerException;
import com.example.nanuri.handler.exception.ErrorCode;
import com.example.nanuri.handler.exception.UnAuthorizedTokenException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

import java.util.Date;
import java.util.Random;

@Component
public class JwtTokenProvider {

    @Value("${jwt.token.secret-key}")
    private String SECRET_KEY;

    @Value("${jwt.token.accessTokenValidityInMilliseconds}")
    private long accessTokenValidityInMilliseconds;

    @Value("${jwt.token.refreshTokenValidityInMilliseconds}")
    private long refreshTokenValidityInMilliseconds;

    public long getRefreshTokenValidityInMilliseconds() {
        return refreshTokenValidityInMilliseconds;
    }

    public long getAccessTokenValidityInMilliseconds() {
        return accessTokenValidityInMilliseconds;
    }

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

    // 인증 성공시 SecurityContextHolder에 저장할 Authentication 객체 생성
    public Authentication getAuthentication(String token) {
        String userId = this.getUserId(token);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userId,null,null);
        return authentication;
    }

    // Jwt Token에서 User PK 추출
    public String getUserId(String token){
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e){
            return e.getClaims().getSubject();
        } catch (JwtException e) {
            throw new UnAuthorizedTokenException(ErrorCode.UNAUTHORIZED_TOKEN);
        }
    }

    public Claims getClamis(String token) {//JWT조회(?)
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)//싸인이 포함된 jwt = jws
                    .getBody();
            return claims;
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("유효기간 만료한 토큰입니다.");
        } catch (JwtException e) {
            throw new RuntimeException("유효하지 않는 토큰입니다.");
        }
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    // Jwt Token의 유효성 및 만료 기간 검사
    public boolean validateToken(String token){
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    //토큰 만료기한 가져오기
    public Date getExpirationDate(String token) {
        Claims claims = getClamis(token);
        return claims.getExpiration();
    }

}
