package com.example.nanuri.config.custom_oauth;

import com.example.nanuri.domain.user.Role;
import lombok.Builder;

public class LoginResponse {
    private Long id;
    private String name;
    private String email;
    private String imageUrl;
    private Role role;
    private String tokenType;
    private String accessToken;
    private String refreshToken;

    @Builder
    public LoginResponse(Long id, String name, String email, String imageUrl, Role role, String tokenType, String accessToken, String refreshToken) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.role = role;
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
