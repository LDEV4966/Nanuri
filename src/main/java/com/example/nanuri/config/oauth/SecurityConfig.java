package com.example.nanuri.config.oauth;

import lombok.AllArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final OAuthService oAuthService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()//h2 접속을 위해
                .and()
                .oauth2Login()// 시작점
                .userInfoEndpoint()// 로그인 성공이후 사용자 정보를 가져올 때 설정 담당
                .userService(oAuthService); //성공 시, 후작업을 진행할 UserService 인터페이스 구현체
    }
}
