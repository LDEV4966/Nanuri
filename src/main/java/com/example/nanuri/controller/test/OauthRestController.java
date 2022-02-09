package com.example.nanuri.controller.test;

import com.example.nanuri.config.custom_oauth.OauthService;
import com.example.nanuri.dto.http.LoginResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class OauthRestController {

    private final OauthService oauthService;

    @PostMapping(path = "/login/oauth/{providerName}")
    public ResponseEntity<LoginResponse>  login(@PathVariable String providerName, @RequestParam String accessToken){
        LoginResponse loginResponse = oauthService.login(providerName,accessToken);
        return ResponseEntity.ok().body(loginResponse);
    }
}
