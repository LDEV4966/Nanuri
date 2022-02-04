package com.example.nanuri.config.custom_oauth;

import com.example.nanuri.domain.user.Role;
import com.example.nanuri.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserProfile {

    private final String oauthId;
    private final String name;
    private final String email;
    private final String imageUrl;

    @Builder
    public UserProfile(String oauthId, String name, String email, String imageUrl) {
        this.oauthId = oauthId;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
    }

    public User toUser(){
        return new User(oauthId,name,email,imageUrl, Role.USER);
    }


}
