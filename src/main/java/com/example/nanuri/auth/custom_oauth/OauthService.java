package com.example.nanuri.auth.custom_oauth;

import com.example.nanuri.auth.jwt.RefreshToken;
import com.example.nanuri.domain.user.User;
import com.example.nanuri.domain.user.UserRepository;
import com.example.nanuri.dto.http.LoginResponse;
import com.example.nanuri.auth.jwt.Token;
import com.example.nanuri.auth.jwt.JwtTokenProvider;
import com.example.nanuri.auth.jwt.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final OauthUserInfoUrl oauthUserInfoUrl;

    private final Map<String,String> userInfoUrl;

    public LoginResponse login(String providerName, String oAuthAccessToken){

        // access token 으로 유저정보 가져오기
        UserProfile userProfile = getUserProfile(providerName,oAuthAccessToken);

        //유저 DB에 저장
        User user = saveOrUpdate(userProfile);

        //우리 앱의 JWT 토큰 만들기
        String accessToken = jwtTokenProvider.createAccessToken(String.valueOf(user.getUserId()));
        String refreshToken = jwtTokenProvider.createRefreshToken();
        Token token = Token.builder()
                .accessToken(accessToken)
                .accessTokenValidityInMilliseconds(jwtTokenProvider.getAccessTokenValidityInMilliseconds())
                .refreshToken(refreshToken)
                .refreshTokenValidityInMilliseconds(jwtTokenProvider.getRefreshTokenValidityInMilliseconds())
                .build();

        // DB에 refreshToken 추가
        refreshTokenService.save(RefreshToken.builder()
                        .userId(user.getUserId().toString())
                        .refreshToken(refreshToken)
                         .build());

        // Login Response
        LoginResponse loginResponse = LoginResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .imageUrl(user.getImageUrl())
                .role(user.getRole())
                .token(token)
                .build();

        return loginResponse;
    }

    private User saveOrUpdate(UserProfile userProfile){
        User user = userRepository.findByOauthId(userProfile.getOauthId())
                .map(m -> m.update(userProfile.getName(), userProfile.getEmail(), userProfile.getImageUrl()))
                .orElse(userProfile.toUser());
        return userRepository.save(user);
    }

    private UserProfile getUserProfile(String providerName, String oAuthAccessToken){
        Map<String,Object> userAttributes = getUserAttributes(providerName,oAuthAccessToken);
        return OauthAttributes.extract(providerName,userAttributes);
    }

    private Map<String,Object> getUserAttributes(String providerName, String oAuthAccessToken){
        return WebClient.create()
                .get()
                .uri(oauthUserInfoUrl.getByProviderName(providerName))
                .headers(header -> header.setBearerAuth(oAuthAccessToken))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String,Object>>() {})
                .block();
    }


}
