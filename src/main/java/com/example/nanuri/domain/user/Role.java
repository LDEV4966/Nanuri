package com.example.nanuri.domain.user;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private String key;

    Role(String key){
        this.key =key;
    }
}
