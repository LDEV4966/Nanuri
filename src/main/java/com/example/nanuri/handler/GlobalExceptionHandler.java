package com.example.nanuri.handler;

import com.example.nanuri.handler.exception.ErrorCode;
import com.example.nanuri.handler.exception.ErrorResponse;
import com.example.nanuri.handler.exception.LessonNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.hibernate.TypeMismatchException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
@RestController
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value =ConstraintViolationException.class)
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

    @ExceptionHandler(value = TypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleTypeMismatchException(){
        log.error("handleTypeMismatchException throw Exception : {}", ErrorCode.TYPE_MISMATCH);
        return ErrorResponse.toResponseEntity(ErrorCode.TYPE_MISMATCH);
    }

    @ExceptionHandler( value = Exception.class)
    protected ResponseEntity<ErrorResponse> handleUnknownException(Exception e) {
        log.error("handleUnknownException throw Exception : {}", ErrorCode.UNKNOWN_ERROR);
        log.error(e.getMessage());
        return ErrorResponse.toResponseEntity(ErrorCode.UNKNOWN_ERROR);
    }
}
