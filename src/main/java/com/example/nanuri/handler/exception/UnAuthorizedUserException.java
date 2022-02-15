package com.example.nanuri.handler.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UnAuthorizedUserException extends RuntimeException{

    private final ErrorCode errorCode;

}
