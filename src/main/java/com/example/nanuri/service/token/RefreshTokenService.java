package com.example.nanuri.service.token;

import com.example.nanuri.auth.jwt.JwtTokenProvider;
import com.example.nanuri.domain.token.RefreshToken;
import com.example.nanuri.domain.token.RefreshTokenRepository;
import com.example.nanuri.dto.token.Token;
import com.example.nanuri.handler.exception.ErrorCode;
import com.example.nanuri.handler.exception.RefreshTokenNotFoundException;
import com.example.nanuri.handler.exception.UnAuthorizedRefreshTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    final private RefreshTokenRepository refreshTokenRepository;
    final private JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void save(RefreshToken refreshToken){
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public Token refreshAccessToken(String userId){
        RefreshToken refreshTokenDto = refreshTokenRepository.findById(userId)
                .orElseThrow(() -> new RefreshTokenNotFoundException(ErrorCode.REFRESH_TOKKEN_NOT_FOUND));
        String refreshToken = refreshTokenDto.getRefreshToken();
        if(jwtTokenProvider.validateToken(refreshToken)){
            //JWT 토큰 만들기
            String accessToken = jwtTokenProvider.createAccessToken(userId);
            Token token = Token.builder()
                    .accessToken(accessToken)
                    .accessTokenValidityInMilliseconds(jwtTokenProvider.getAccessTokenValidityInMilliseconds())
                    .refreshToken(refreshToken)
                    .refreshTokenValidityInMilliseconds(jwtTokenProvider.getExpirationDate( refreshToken).getTime() - new Date().getTime())
                    .build();
            return token;
        } else {
            // 리프레시 토큰 만료되면 해당 유저가 재 로그인시 refreshToken 테이블 정보도 업데이트 됨.
            throw new UnAuthorizedRefreshTokenException(ErrorCode.UNAUTHORIZED_REFRESHTOKEN);
        }
    }
}
