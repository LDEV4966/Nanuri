package com.example.nanuri.domain.user;

import lombok.Getter;

@Getter
public enum Role {
    GUEST("ROLE_GUEST"),
    USER("ROLE_USER");

    private String key;

    Role(String key){
        this.key =key;
    }
}
