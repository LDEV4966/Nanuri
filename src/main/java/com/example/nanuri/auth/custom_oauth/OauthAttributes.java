package com.example.nanuri.auth.custom_oauth;

import java.util.Arrays;
import java.util.Map;

public enum OauthAttributes {

    GOOGLE("google"){
        @Override
        public UserProfile of(Map<String, Object> attributes) {
            return UserProfile.builder()
                    .oauthId(String.valueOf(attributes.get("sub")))
                    .email((String) attributes.get("email"))
                    .name((String) attributes.get("name"))
                    .imageUrl((String) attributes.get("picture"))
                    .build();

        }
    },
    NAVER("naver"){
        @Override
        public UserProfile of(Map<String, Object> attributes) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            return UserProfile.builder()
                    .oauthId((String) response.get("id"))
                    .email((String) response.get("email"))
                    .name((String) response.get("name"))
                    .imageUrl((String) response.get("profile_image"))
                    .build();
        }

    },
    KAKAO("kakao"){
        @Override
        public UserProfile of(Map<String, Object> attributes) {
            String id = String.valueOf(attributes.get("id"));
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");
            return UserProfile.builder()
                    .oauthId(id)
                    .email((String) kakaoAccount.get("email"))
                    .name((String) kakaoProfile.get("nickname"))
                    .imageUrl((String)kakaoProfile.get("profile_image_url"))
                    .build();
        }
    }
    ;
    private final String providerName;

    OauthAttributes(String providerName){
        this.providerName = providerName;
    }

    public static UserProfile extract(String providerName, Map<String,Object> attributes){
        return Arrays.stream(values())
                .filter(provider -> providerName.equals(provider.providerName))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of(attributes);
    }

    public abstract UserProfile of(Map<String,Object> attributes);

}
