package com.example.nanuri.controller.test;

import com.example.nanuri.dto.http.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {
    @GetMapping("/hello") // ResponseEntity<? extends BasicResponse> : BasicResponse를 상속한 ?(자식 클래스)를 위한 제네릭
    public ResponseEntity<? extends BasicResponse> Hello(){
        boolean isTrue = true;
        if(isTrue) {
            return ResponseEntity.ok().body(new SuccessDataResponse<String>("hello"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(MessageEnum.NOT_FOUND, HttpStatusEnum.NOT_FOUND));
    }
}
