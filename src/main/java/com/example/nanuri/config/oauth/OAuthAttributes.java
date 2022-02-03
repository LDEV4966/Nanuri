package com.example.nanuri.config.oauth;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum OAuthAttributes {
    GOOGLE("google",(attributes) -> {
       return new UserProfille(
               String.valueOf(attributes.get("sub")),
               (String) attributes.get("name"),
               (String) attributes.get("email"),
               (String) attributes.get("picture")
       );
    }),
    NAVER("naver",(attributes) -> {
        Map<String,Object> response = (Map<String, Object>) attributes.get("response");
        return new UserProfille(
                String.valueOf(attributes.get("id")),
                (String) attributes.get("name"),
                (String) attributes.get("email"),
                (String) attributes.get("profille_image")
        );
    });
    private final String registrationId;
    private final Function<Map<String,Object>,UserProfille> of;

    OAuthAttributes(String registrationId, Function<Map<String,Object>,UserProfille> of){
        this.registrationId = registrationId;
        this.of = of;
    }

    public static UserProfille extract(String registrationId, Map<String,Object> attributes){
        return Arrays.stream(values())
                .filter(provider -> registrationId.equals(provider.registrationId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of.apply(attributes);
    }

}
