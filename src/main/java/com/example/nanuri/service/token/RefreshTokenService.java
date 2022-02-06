package com.example.nanuri.service.token;

import com.example.nanuri.domain.token.RefreshToken;
import com.example.nanuri.domain.token.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {

    final private RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void save(RefreshToken refreshToken){
        refreshTokenRepository.save(refreshToken);
    }
}
