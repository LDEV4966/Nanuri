package com.example.nanuri.controller.auth.api;

import com.example.nanuri.auth.custom_oauth.OauthService;
import com.example.nanuri.auth.jwt.JwtTokenProvider;
import com.example.nanuri.dto.http.LoginResponse;

import com.example.nanuri.auth.jwt.Token;
import com.example.nanuri.handler.exception.AuthenticationNullPointerException;
import com.example.nanuri.handler.exception.ErrorCode;
import com.example.nanuri.auth.jwt.RefreshTokenService;
import com.example.nanuri.handler.exception.TokenTypeMismatchException;
import com.example.nanuri.handler.exception.UnAuthorizedTokenException;
import lombok.RequiredArgsConstructor;
import org.hibernate.TypeMismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.ServerRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class AuthApiController {

    private final OauthService oauthService;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping(path = "/login/oauth/{providerName}")
    public ResponseEntity<LoginResponse>  login(@PathVariable String providerName, @RequestParam String accessToken){
        LoginResponse loginResponse = oauthService.login(providerName,accessToken);
        return ResponseEntity.ok().body(loginResponse);
    }

    @GetMapping(path = "/token")
    public Token refreshAccessToken(@RequestHeader Map<String,Object> requestHeader){

        String authHeader = requestHeader.get("x-auth-token").toString();
        if(!authHeader.startsWith("Bearer ")) {
            throw new TokenTypeMismatchException(ErrorCode.TOKEN_TYPE_MISMATCH); //400
        }
        String accessToken = authHeader.substring(7,authHeader.length());
        String userId = jwtTokenProvider.getUserId(accessToken);
        Token token = refreshTokenService.refreshAccessToken(userId);
        return token;
    }
}
