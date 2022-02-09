package com.example.nanuri.auth.jwt;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Token {
    private String tokenType;
    private String accessToken;
    private long accessTokenValidityInMilliseconds;
    private String refreshToken;
    private long refreshTokenValidityInMilliseconds;

    @Builder
    public Token(String accessToken, long accessTokenValidityInMilliseconds, String refreshToken, long refreshTokenValidityInMilliseconds) {
        this.tokenType = "Bearer";
        this.accessToken = accessToken;
        this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds;
        this.refreshToken = refreshToken;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds;
    }
}
