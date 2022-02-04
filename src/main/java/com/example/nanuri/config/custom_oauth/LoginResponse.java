package com.example.nanuri.config.custom_oauth;

import com.example.nanuri.domain.user.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {
    private Long userId;
    private String name;
    private String email;
    private String imageUrl;
    private Role role;
    private String tokenType;
    private String accessToken;
    private String refreshToken;

    @Builder
    public LoginResponse(Long userId, String name, String email, String imageUrl, Role role, String tokenType, String accessToken, String refreshToken) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.role = role;
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", role=" + role +
                ", tokenType='" + tokenType + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
