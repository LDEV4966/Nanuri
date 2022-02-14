package com.example.nanuri.handler.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenTypeMismatchException extends RuntimeException{
    private final ErrorCode errorCode;
}
