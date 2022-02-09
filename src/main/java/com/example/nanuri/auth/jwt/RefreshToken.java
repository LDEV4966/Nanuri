package com.example.nanuri.auth.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@Getter
@Entity
public class RefreshToken {

    @Id
    private String userId;

    @Column(length = 500)
    private String RefreshToken;

    @Builder
    public RefreshToken(String userId, String refreshToken) {
        this.userId = userId;
        RefreshToken = refreshToken;
    }
}
