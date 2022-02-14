package com.example.nanuri.handler.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ParticipantNotFoundException extends RuntimeException{
    private final ErrorCode errorCode;
}
