package com.example.nanuri.controller.test;

import com.example.nanuri.dto.http.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {
    @GetMapping("/hello") // ResponseEntity<? extends BasicResponse> : BasicResponse를 상속한 ?(자식 클래스)를 위한 제네릭
    public ResponseEntity<? extends BasicResponse> Hello(){
        boolean isTrue = false;
        if(!isTrue) {
//            throw new ("hello가 false입니다.");
        }
        return ResponseEntity.ok().body(new SuccessDataResponse<String>("hello"));
    }
}
