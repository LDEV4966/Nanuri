package com.example.nanuri.controller.auth.api;

import com.example.nanuri.auth.custom_oauth.OauthService;
import com.example.nanuri.dto.http.LoginResponse;

import com.example.nanuri.dto.token.Token;
import com.example.nanuri.handler.exception.AuthenticationNullPointerException;
import com.example.nanuri.handler.exception.ErrorCode;
import com.example.nanuri.service.token.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class AuthApiController {

    private final OauthService oauthService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping(path = "/login/oauth/{providerName}")
    public ResponseEntity<LoginResponse>  login(@PathVariable String providerName, @RequestParam String accessToken){
        LoginResponse loginResponse = oauthService.login(providerName,accessToken);
        return ResponseEntity.ok().body(loginResponse);
    }

    @GetMapping(path = "/token")
    public Token refreshAccessToken(Authentication authentication){
        if(authentication == null) {
            throw new AuthenticationNullPointerException(ErrorCode.NULL_AUTHENTICATION);
        }
        Token token = refreshTokenService.refreshAccessToken(authentication.getName());
        return token;
    }
}
