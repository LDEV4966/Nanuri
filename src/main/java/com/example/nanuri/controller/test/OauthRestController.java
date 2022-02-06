package com.example.nanuri.controller.test;

import com.example.nanuri.config.custom_oauth.OauthService;
import com.example.nanuri.dto.http.LoginResponse;

import com.example.nanuri.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OauthRestController {

    private final OauthService oauthService;

    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping(path = "/login/oauth/{providerName}")
    public ResponseEntity<LoginResponse> login(@PathVariable String providerName, @RequestParam String code){
        LoginResponse loginResponse = oauthService.login(providerName,code);
        return ResponseEntity.ok().body(loginResponse);
    }
}
