package com.example.nanuri.config.custom_oauth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Getter
@ConfigurationProperties(prefix = "oauth2")
public class OauthProperties {

    private final Map<String,Client> client = new HashMap<>();
    private final Map<String,Provider> provider = new HashMap<>();


    @Getter
    @Setter
    public static class Client {
        private String clientId;
        private String clientSecret;
        private String redirectUri;
    }

    @Getter
    @Setter
    public static class Provider {
        private String tokenUri;
        private String userInfoUri;
        private String userNameAttribute;
    }


}
