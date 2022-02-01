package com.example.nanuri.handler.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class LessonNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;
}
