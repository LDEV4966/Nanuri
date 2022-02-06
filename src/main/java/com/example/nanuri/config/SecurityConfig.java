package com.example.nanuri.config;

import com.example.nanuri.config.jwt.JwtAuthenticationFilter;
import com.example.nanuri.config.jwt.JwtTokenProvider;
import com.example.nanuri.handler.exception.AuthenticationEntryPointException;
import com.example.nanuri.handler.exception.ErrorCode;
import com.example.nanuri.handler.exception.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtTokenProvider);

        http
                .formLogin().disable()
                .httpBasic().disable()
                .cors().disable()
                .csrf().disable()
                .headers().frameOptions().disable();
        http
                .authorizeRequests()
                .antMatchers( "/","/h2-console/**","/login/**").permitAll()
                .antMatchers(HttpMethod.GET,"/lesson/**").permitAll()
                .antMatchers(HttpMethod.GET,"/token").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                        response.setContentType("application/json;charset=UTF-8");
                        ErrorCode errorCode = ErrorCode.AUTH_ENTRY_DENIED;
                        response.setStatus(errorCode.getHttpStatus().value());
                        response.getWriter().println("{ \"timestamp\" : \"" + LocalDateTime.now()
                                + "\", \"status\" : " +  errorCode.getHttpStatus().value()
                                + ", \"error\" : \"" + errorCode.getHttpStatus().name()
                                + "\", \"code\" : \"" + errorCode.name()
                                + "\", \"message\" : \"" +  errorCode.getDetail() + "\" }");
                    }
                })
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement()//세션관리 설정
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);//서버에 값을 세션값을 저장하지 않고 stateless로 설정함


    }

    @Bean//BCryptPasswordEncoder빈 등록
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
