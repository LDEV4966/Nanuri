package com.example.nanuri.config.custom_oauth;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@EnableConfigurationProperties(OauthProperties.class) //Component Scan 이후에 일어남.
@AllArgsConstructor
public class OauthConfig {

    private final OauthProperties properties;

    // OauthConfig에서 빈으로 등록 된 OauthProperties를 주입받아 OauthAdapter를 사용해 각 Oauth 서버 정보를 가진 OauthProvider로 분해하여
    // InMemoryProviderRepository에 저장한다.
    // 앱이 실행될 때, OAuth 서버 정보들은 객체로 만들어 메모리에 저장된다.
    @Bean
    public InMemoryProviderRepository inMemoryProviderRepository() {
        Map<String, OauthProvider> providers = OauthAdapter.getOauthProviders(properties);
        return new InMemoryProviderRepository(providers);
    }
}
