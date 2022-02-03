package com.example.nanuri.config.oauth;

import com.example.nanuri.domain.user.User;
import com.example.nanuri.domain.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@AllArgsConstructor
@Service
public class OAuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest); // OAuth 서비스 (구글,네이버) 에서 가져온 기본 유저정보를 담고 있음

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName(); // OAuth 로그인 시 키가 되는 값.

        Map<String,Object> attributes = oAuth2User.getAttributes();

        // OAuth 서비스에서 얻은 이름과 정보로  OAuth 서비스에 종속적이지 않은 공통된 UserProfile 이라는 객체를 만든다.
        UserProfille userProfille = OAuthAttributes.extract(registrationId,attributes);

        User user = saveOrUpdate(userProfille);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes,
                userNameAttributeName
        );
    }

    private User saveOrUpdate(UserProfille userProfille){
        User user = userRepository.findByOauthId(userProfille.getOauthId())
                .map(m -> m.update(userProfille.getName(),userProfille.getEmail(),userProfille.getImageUrl()))
                .orElse(userProfille.toUser());
        return userRepository.save(user);
    }
}
