package com.example.nanuri;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class NanuriApplication {

    @GetMapping("/")
    public String home( ) {
        return "nanuri server is running ...";
    }

    public static void main(String[] args) {
        SpringApplication.run(NanuriApplication.class, args);
    }

}
