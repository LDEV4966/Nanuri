package com.example.nanuri.dto.http;

public enum HttpStatusEnum {
    OK(200),
    BAD_REQUEST(400),
    NOT_FOUND(404),
    INTERNAL_SERER_ERROR(500);

    int statusCode;

    HttpStatusEnum(int statusCode) {
        this.statusCode = statusCode;
    }
}
