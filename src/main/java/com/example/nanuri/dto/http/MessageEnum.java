package com.example.nanuri.dto.http;

public enum MessageEnum {
    OK("성공적으로 처리되었습니다."),
    BAD_REQUEST("잘못된 요청입니다."),
    NOT_FOUND("해당 자료가 없습니다."),
    INTERNAL_SERER_ERROR("서버 내부 오류입니다.");

    String message;

    MessageEnum(String message) {
        this.message = message;
    }
}
