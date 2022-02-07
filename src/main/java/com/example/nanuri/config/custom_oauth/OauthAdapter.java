package com.example.nanuri.config.custom_oauth;

import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class OauthAdapter {

    //OauthProperties를 OauthProvider로 변환 해준다.
    public static Map<String, OauthProvider> getOauthProviders(OauthProperties properties) {

        Map<String, OauthProvider> oauthProvider = new HashMap<>();

        properties.getClient().forEach((key,value) ->
                oauthProvider.put(key, new OauthProvider(value,properties.getProvider().get(key))));


        return oauthProvider;
    }
}
