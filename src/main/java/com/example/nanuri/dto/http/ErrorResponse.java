package com.example.nanuri.dto.http;

import lombok.Getter;

@Getter
public class ErrorResponse extends BasicResponse {
    private String errorMessage;
    private int status;

    public ErrorResponse(MessageEnum message, HttpStatusEnum errorCode) {
        this.errorMessage = message.message;
        this.status = errorCode.statusCode;
    }
}
