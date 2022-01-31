package com.example.nanuri.handler;

import com.example.nanuri.dto.http.BasicResponse;
import com.example.nanuri.dto.http.ErrorResponse;
import com.example.nanuri.dto.http.HttpStatusEnum;
import com.example.nanuri.dto.http.MessageEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<? extends BasicResponse> handleArgumentException(Exception e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(MessageEnum.INTERNAL_SERER_ERROR, HttpStatusEnum.INTERNAL_SERER_ERROR));
    }
}
