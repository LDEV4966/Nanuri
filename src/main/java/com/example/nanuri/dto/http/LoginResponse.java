package com.example.nanuri.dto.http;

import com.example.nanuri.domain.user.Role;
import com.example.nanuri.auth.jwt.Token;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {
    private Long userId;
    private String name;
    private String email;
    private String imageUrl;
    private Role role;
    private Token token;

    @Builder
    public LoginResponse(Long userId, String name, String email, String imageUrl, Role role, Token token) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.role = role;
        this.token = token;
    }

}
