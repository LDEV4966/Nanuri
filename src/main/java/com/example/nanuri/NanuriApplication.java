package com.example.nanuri;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class NanuriApplication {

    @GetMapping("/")
    public String home( ) {
        return "nanuri server is running ...";
    }

    private static final String PROPERTIES =
            "spring.config.location="
                    +"classpath:application.properties,"
                    +"classpath:aws.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(NanuriApplication.class)
                .profiles(PROPERTIES)
                .run(args);
    }

}
