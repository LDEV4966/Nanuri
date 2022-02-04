package com.example.nanuri.controller.test;

import com.example.nanuri.config.custom_oauth.OauthService;
import com.example.nanuri.config.custom_oauth.LoginResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OauthRestController {

    private final OauthService customOauthService;

    @GetMapping(path = "/login/oauth/{providerName}")
    public ResponseEntity<LoginResponse> login(@PathVariable String providerName, @RequestParam String code){
        LoginResponse loginResponse = customOauthService.login(providerName,code);
        return ResponseEntity.ok().body(loginResponse);
    }
}
