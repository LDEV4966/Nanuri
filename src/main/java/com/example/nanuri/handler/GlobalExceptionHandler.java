package com.example.nanuri.handler;

import com.example.nanuri.handler.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.hibernate.TypeMismatchException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
@RestController
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = AuthenticationEntryPointException.class)
    protected ResponseEntity<ErrorResponse> handleAuthenticationEntryPointException() {
        log.error("handleFobiddenAuthenticationException throw Exception : {}", ErrorCode.AUTH_ENTRY_DENIED);
        return ErrorResponse.toResponseEntity(ErrorCode.AUTH_ENTRY_DENIED);
    }

    @ExceptionHandler(value = UnAuthorizedTokenException.class)
    protected ResponseEntity<ErrorResponse> handleUnAuthorizedTokenException() {
        log.error("handleUnAuthorizedTokenException throw Exception : {}", ErrorCode.UNAUTHORIZED_TOKEN);
        return ErrorResponse.toResponseEntity(ErrorCode.UNAUTHORIZED_TOKEN);
    }

    @ExceptionHandler(value = UnAuthorizedRefreshTokenException.class)
    protected ResponseEntity<ErrorResponse> handleUnAuthorizedRefreshTokenException() {
        log.error("handleUnAuthorizedRefreshTokenException throw Exception : {}", ErrorCode.UNAUTHORIZED_REFRESHTOKEN);
        return ErrorResponse.toResponseEntity(ErrorCode.UNAUTHORIZED_REFRESHTOKEN);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    protected ResponseEntity<ErrorResponse> handleConstraintViolationException() {
        log.error("handleConstraintViolationException throw Exception : {}", ErrorCode.CONSTRAINT_VIOLATION_RESOURCE);
        return ErrorResponse.toResponseEntity(ErrorCode.CONSTRAINT_VIOLATION_RESOURCE);
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    protected ResponseEntity<ErrorResponse> handleDataIntegrityViolationException() {
        log.error("handleDataIntegrityViolationException throw Exception : {}", ErrorCode.INTEGRITY_VIOLATION_RESOURCE);
        return ErrorResponse.toResponseEntity(ErrorCode.INTEGRITY_VIOLATION_RESOURCE);
    }

    @ExceptionHandler(value = NullPointerException.class)
    protected ResponseEntity<ErrorResponse> handleNullPointerException() {
        log.error("handleNullPointerException throw Exception : {}", ErrorCode.NULL_POINTER);
        return ErrorResponse.toResponseEntity(ErrorCode.NULL_POINTER);
    }

    @ExceptionHandler(value = AuthenticationNullPointerException.class)
    protected ResponseEntity<ErrorResponse> handleAuthenticationNullPointerException() {
        log.error("handleNullPointerException throw Exception : {}", ErrorCode.NULL_AUTHENTICATION);
        return ErrorResponse.toResponseEntity(ErrorCode.NULL_AUTHENTICATION);
    }

    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    protected ResponseEntity<ErrorResponse> handleFileSizeLimitExceededException() {
        log.error("handleFileSizeLimitExceededException throw Exception : {}", ErrorCode.MAX_UPLOAD_SIZE);
        return ErrorResponse.toResponseEntity(ErrorCode.MAX_UPLOAD_SIZE);
    }

    @ExceptionHandler(value = LessonNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleLessonNotFoundException(){
        log.error("handleLessonNotFoundException throw Exception : {}", ErrorCode.LESSON_NOT_FOUND);
        return ErrorResponse.toResponseEntity(ErrorCode.LESSON_NOT_FOUND);
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleUserNotFoundException(){
        log.error("handleUserNotFoundException throw Exception : {}", ErrorCode.USER_NOT_FOUND);
        return ErrorResponse.toResponseEntity(ErrorCode.USER_NOT_FOUND);
    }

    @ExceptionHandler(value = TokenNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleTokenNotFoundException(){
        log.error("handleTokenNotFoundException throw Exception : {}", ErrorCode.TOKKEN_NOT_FOUND);
        return ErrorResponse.toResponseEntity(ErrorCode.TOKKEN_NOT_FOUND);
    }

    @ExceptionHandler(value = RefreshTokenNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleRefreshTokenNotFoundException(){
        log.error("handleRefreshTokenNotFoundException throw Exception : {}", ErrorCode.REFRESH_TOKKEN_NOT_FOUND);
        return ErrorResponse.toResponseEntity(ErrorCode.REFRESH_TOKKEN_NOT_FOUND);
    }

    @ExceptionHandler(value = TypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleTypeMismatchException(){
        log.error("handleTypeMismatchException throw Exception : {}", ErrorCode.TYPE_MISMATCH);
        return ErrorResponse.toResponseEntity(ErrorCode.TYPE_MISMATCH);
    }

    @ExceptionHandler( value = Exception.class)
    protected ResponseEntity<ErrorResponse> handleUnknownException(Exception e) {
        log.error("handleUnknownException throw Exception : {}", ErrorCode.UNKNOWN_ERROR);
        return ErrorResponse.toResponseEntity(ErrorCode.UNKNOWN_ERROR);
    }


}
