package com.karrier.mentoring.http.error;

import com.karrier.mentoring.http.ErrorResponse;
import com.karrier.mentoring.http.error.exception.MemberNotFoundException;
import com.karrier.mentoring.http.error.exception.UnAuthorizedMemberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
@RestController
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // 401
    @ExceptionHandler(value = MemberNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleUnAuthorizedMemberException(HttpServletRequest request, MemberNotFoundException memberNotFoundException) {
        log.error("ErrorExceptionURI : " + request.getRequestURI());
        log.error("handleUnAuthorizedAccessException throw Exception : {}", memberNotFoundException.getErrorCode());
        return ErrorResponse.toResponseEntity(memberNotFoundException.getErrorCode(),request);
    }


    // 404
    @ExceptionHandler(value = UnAuthorizedMemberException.class)
    protected ResponseEntity<ErrorResponse> handleAuthenticationForbiddenException(HttpServletRequest request, UnAuthorizedMemberException unAuthorizedMemberException) {
        log.error("ErrorExceptionURI : " + request.getRequestURI());
        log.error("handleAuthenticationForbiddenException throw Exception : {}", unAuthorizedMemberException.getErrorCode());
        return ErrorResponse.toResponseEntity(unAuthorizedMemberException.getErrorCode(),request);
    }

    //500
    @ExceptionHandler( value = Exception.class)
    protected ResponseEntity<ErrorResponse> handleUnknownException(HttpServletRequest request,Exception e) {
        log.error("ErrorExceptionURI : " + request.getRequestURI());
        log.error("handleUnknownException throw Exception : {}", ErrorCode.UNKNOWN_ERROR);
        return ErrorResponse.toResponseEntity(ErrorCode.UNKNOWN_ERROR,request);
    }


}

