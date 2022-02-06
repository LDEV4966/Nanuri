package com.example.nanuri.handler.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /* 400 BAD_REQUEST : 잘못된 요청 */
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 기입 정보입니다."),
    TYPE_MISMATCH(HttpStatus.BAD_REQUEST,"기대된 class Type으로 값을 등록할 수 없습니다 "),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    UNAUTHORIZED_MEMBER(HttpStatus.UNAUTHORIZED, "권한이 없는 사용자 입니다."),

    /* 403 FORBIDDEN : 해당 권한으로 접근이 허락 되지 않는 정보*/
    FORBIDDEN_RESOURCE(HttpStatus.FORBIDDEN, "접근이 불가능한 자원 입니다."),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다"),
    LESSON_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 레슨 정보를 찾을 수 없습니다"),

    /* 409 CONFLICT : DB 데이터 관리 충돌 */
    CONSTRAINT_VIOLATION_RESOURCE(HttpStatus.CONFLICT, "DB 제약조건 위반입니다."),
    INTEGRITY_VIOLATION_RESOURCE(HttpStatus.CONFLICT, "DB 무결성 위반입니다."),

    /* 500 핸들링 하지 않은 에러 */
    NULL_POINTER(HttpStatus.INTERNAL_SERVER_ERROR,"NULL 포인터에 대한 접근이 있습니다."),
    MAX_UPLOAD_SIZE(HttpStatus.INTERNAL_SERVER_ERROR,"업로드 가능한 최대용량을 초과 했습니다."),
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"서버 내부 UNKNOWN 에러입니다.");

    ;

    private final HttpStatus httpStatus;
    private final String detail;
}
