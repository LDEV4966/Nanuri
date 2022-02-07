package com.example.nanuri.handler.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthenticationEntryPointException extends RuntimeException{ //인가 실패
    private final ErrorCode errorCode;
}
