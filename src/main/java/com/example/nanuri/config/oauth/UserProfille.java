package com.example.nanuri.config.oauth;

import com.example.nanuri.domain.user.Role;
import com.example.nanuri.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserProfille {

    private final String oauthId;
    private final String name;
    private final String email;
    private final String imageUrl;

    public User toUser(){
        return new User(oauthId,name,email,imageUrl, Role.USER);
    }
}
