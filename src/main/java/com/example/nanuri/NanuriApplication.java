package com.example.nanuri;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class NanuriApplication {

    @GetMapping("/") // ResponseEntity<? extends BasicResponse> : BasicResponse를 상속한 ?(자식 클래스)를 위한 제네릭
    public String mainResponse(){
        return  "Nanuri server is running";
    }

    public static void main(String[] args) {
        SpringApplication.run(NanuriApplication.class, args);
    }

}
