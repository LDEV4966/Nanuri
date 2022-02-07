package com.example.nanuri.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    //Spring Security Filter와 통합하지 않고 사용자가 정의한 필터(JwtAuthenticationFilter)에서 인증 및 권한 작업을 진행할 것이기
    //때문에 AuthenticationManager를 사용하지 않고 JwtTokenProvider를 통해서 인증 후 SecurityContextHolder를 바로 사용했다.
    // Request로 들어오는 Jwt Token의 유효성을 검증하는 filter를 filterChain에 등록합니다.
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String authHeader = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        if (authHeader != null ) {   // token 검증
            if (authHeader.startsWith("Bearer ")){ // 토큰 타입 Bearer 인증 -> NULL_AUTHENTICATION
                String token = authHeader.substring(7, authHeader.length());
                if(jwtTokenProvider.validateToken(token)) { // 토큰 유효한지 인증 -> NULL_AUTHENTICATION
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);    // 인증 객체 생성
                    SecurityContextHolder.getContext().setAuthentication(authentication); // SecurityContextHolder에 인증 객체 저장
                }
            }
        }
        chain.doFilter(request,response);
    }
}
