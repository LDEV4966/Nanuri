package com.example.nanuri.controller.token;

import com.example.nanuri.dto.token.Token;
import com.example.nanuri.handler.exception.AuthenticationNullPointerException;
import com.example.nanuri.handler.exception.ErrorCode;
import com.example.nanuri.service.token.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenApiController {

    private final RefreshTokenService refreshTokenService;

    @GetMapping(path = "/token")
    public Token refreshAccessToken(Authentication authentication){
        if(authentication == null) {
            throw new AuthenticationNullPointerException(ErrorCode.NULL_AUTHENTICATION);
        }
        Token token = refreshTokenService.refreshAccessToken(authentication.getName());
        return token;
    }
}
