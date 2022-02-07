package com.example.nanuri.controller.test;

import com.example.nanuri.dto.http.*;
//import com.example.nanuri.jwt.JwtUserAuthentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class HelloController {
    @GetMapping("/hello") // ResponseEntity<? extends BasicResponse> : BasicResponse를 상속한 ?(자식 클래스)를 위한 제네릭
    public String Hello(Authentication authentication){
        return  authentication.getName();
    }
}
