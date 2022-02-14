package com.example.nanuri.handler.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /* 400 BAD_REQUEST : 잘못된 요청 */
    TYPE_MISMATCH(HttpStatus.BAD_REQUEST,"기대된 class Type으로 값을 등록할 수 없습니다."),
    TOKEN_TYPE_MISMATCH(HttpStatus.BAD_REQUEST,"Token Type이 Bearer이 아닙니다."),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    UNAUTHORIZED_MEMBER(HttpStatus.UNAUTHORIZED, "권한이 없는 사용자 입니다."),
    UNAUTHORIZED_TOKEN(HttpStatus.UNAUTHORIZED,"권한이 없는 토큰 입니다."),
    UNAUTHORIZED_REFRESHTOKEN(HttpStatus.UNAUTHORIZED,"권한이 없는 리프레시 토큰 입니다."),

    /* 403 FORBIDDEN : 해당 권한으로 접근이 허락 되지 않는 정보*/
    FORBIDDEN_AUTHENTICATION(HttpStatus.FORBIDDEN, "권한이 없어 접근이 불가능합니다."),
    AUTH_ENTRY_DENIED(HttpStatus.FORBIDDEN, "로그인 없이는 접근이 불가능한 경로이거나, 유효한 토큰이 아닙니다."),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저 정보를 찾을 수 없습니다."),
    LESSON_NOT_FOUND(HttpStatus.NOT_FOUND,"레슨 정보를 찾을 수 없습니다."),
    TOKKEN_NOT_FOUND(HttpStatus.NOT_FOUND,"토큰 정보를 찾을 수 없습니다."),
    REFRESH_TOKKEN_NOT_FOUND(HttpStatus.NOT_FOUND,"리프레시 토큰 정보를 찾을 수 없습니다."),

    /* 409 CONFLICT : DB 데이터 관리 충돌 */
    CONSTRAINT_VIOLATION_RESOURCE(HttpStatus.CONFLICT, "DB 제약조건 위반입니다."),
    INTEGRITY_VIOLATION_RESOURCE(HttpStatus.CONFLICT, "DB 무결성 위반입니다."),

    /* 500 핸들링 하지 않은 에러 */
    NULL_AUTHENTICATION(HttpStatus.INTERNAL_SERVER_ERROR,"토큰 정보가 유효하지 않아, AUTHENTICATION 값이 NULL 입니다."),
    NULL_POINTER(HttpStatus.INTERNAL_SERVER_ERROR,"NULL 포인터에 대한 접근이 있습니다."),
    MAX_UPLOAD_SIZE(HttpStatus.INTERNAL_SERVER_ERROR,"업로드 가능한 최대용량을 초과 했습니다."),
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"서버 내부 UNKNOWN 에러입니다.");

    ;

    private final HttpStatus httpStatus;
    private final String detail;
}
