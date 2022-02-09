package com.example.nanuri.auth.custom_oauth;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Component
public class OauthUserInfoUrl {

    private final Map<String,String> urlMap;

    public OauthUserInfoUrl() {
        this.urlMap = new HashMap<String,String>();
        urlMap.put("naver","https://openapi.naver.com/v1/nid/me");
        urlMap.put("kakao","https://kapi.kakao.com/v2/user/me");
        urlMap.put("google","https://www.googleapis.com/oauth2/v3/userinfo");
    }

    public String getByProviderName(String providerName){
        return this.urlMap.get(providerName);
    };
}
