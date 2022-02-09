package com.example.nanuri.auth.jwt;

import com.example.nanuri.auth.jwt.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,String> {
}
