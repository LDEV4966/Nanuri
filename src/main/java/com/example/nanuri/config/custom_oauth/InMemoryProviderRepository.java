package com.example.nanuri.config.custom_oauth;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class InMemoryProviderRepository {

    private final Map<String, OauthProvider> providers;

    public InMemoryProviderRepository(Map<String, OauthProvider> providers) {
        this.providers = new HashMap<>(providers);
    }

    public OauthProvider findByProviderName(String name){
        return providers.get(name);
    }

}
