package com.example.nanuri.handler.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthenticationNullPointerException extends RuntimeException{
    private final ErrorCode errorCode;
}
