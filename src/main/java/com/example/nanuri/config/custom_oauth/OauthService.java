package com.example.nanuri.config.custom_oauth;

import com.example.nanuri.domain.token.RefreshToken;
import com.example.nanuri.domain.user.User;
import com.example.nanuri.domain.user.UserRepository;
import com.example.nanuri.dto.http.LoginResponse;
import com.example.nanuri.dto.token.Token;
import com.example.nanuri.config.jwt.JwtTokenProvider;
import com.example.nanuri.service.token.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

@Service
@AllArgsConstructor
public class OauthService {

    private final InMemoryProviderRepository customInMemoryProviderRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    public LoginResponse login(String providerName, String code){

        OauthProvider provider = customInMemoryProviderRepository.findByProviderName(providerName);

        // access token 가져오기
        OauthTokenResponse tokenResponse = getToken(code,provider);

        // access token 으로 유저정보 가져오기
        UserProfile userProfile = getUserProfile(providerName,tokenResponse,provider);

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

    private UserProfile getUserProfile(String providerName,OauthTokenResponse tokenResponse, OauthProvider provider ){
        Map<String,Object> userAttributes = getUserAttributes(provider,tokenResponse);
        return OAuthAttributes.extract(providerName,userAttributes);
    }
    private Map<String,Object> getUserAttributes(OauthProvider provider, OauthTokenResponse tokenResponse){
        return WebClient.create()
                .get()
                .uri(provider.getUserInfoUrl())
                .headers(header -> header.setBearerAuth(tokenResponse.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String,Object>>() {})
                .block();
    }

    private OauthTokenResponse getToken(String code, OauthProvider provider){
        //webflux를 사용한 서버 통신
        return WebClient.create()
                .post()
                .uri(provider.getTokenUrl())
                .headers( header -> {
                    header.setBasicAuth(provider.getClientId(),provider.getClientSecret());
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(tokenRequest(code,provider))
                .retrieve()
                .bodyToMono(OauthTokenResponse.class)
                .block();

    }

    private MultiValueMap<String,String> tokenRequest(String code, OauthProvider provider){
        MultiValueMap<String,String> formData = new LinkedMultiValueMap<>();
        formData.add("code",code);
        formData.add("grant_type","authorization_code");
        formData.add("redirect_uri",provider.getRedirectUrl());
        return formData;
    }

}
