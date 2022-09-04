package com.karrier.mentoring.http.error;

import com.karrier.mentoring.http.ErrorResponse;
import com.karrier.mentoring.http.error.exception.*;
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

    //400
    @ExceptionHandler(value = BadRequestException.class)
    protected ResponseEntity<ErrorResponse> handleBadRequestException(HttpServletRequest request, BadRequestException badRequestException) {
        log.error("ErrorExceptionURI : " + request.getRequestURI());
        log.error("handleBadRequestException throw Exception : {}", badRequestException.getErrorCode());
        return ErrorResponse.toResponseEntity(badRequestException.getErrorCode(),request);
    }

    // 401
    @ExceptionHandler(value = UnAuthorizedException.class)
    protected ResponseEntity<ErrorResponse> handleUnAuthorizedException(HttpServletRequest request, UnAuthorizedException unAuthorizedException) {
        log.error("ErrorExceptionURI : " + request.getRequestURI());
        log.error("handleUnAuthorizedException throw Exception : {}", unAuthorizedException.getErrorCode());
        return ErrorResponse.toResponseEntity(unAuthorizedException.getErrorCode(),request);
    }

    // 403
    @ExceptionHandler(value = ForbiddenException.class)
    protected ResponseEntity<ErrorResponse> handleForbiddenException(HttpServletRequest request, ForbiddenException forbiddenException) {
        log.error("ErrorExceptionURI : " + request.getRequestURI());
        log.error("handleForbiddenException throw Exception : {}", forbiddenException.getErrorCode());
        return ErrorResponse.toResponseEntity(forbiddenException.getErrorCode(),request);
    }

    // 404
    @ExceptionHandler(value = NotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundExceptionException(HttpServletRequest request, NotFoundException notFoundException) {
        log.error("ErrorExceptionURI : " + request.getRequestURI());
        log.error("handleNotFoundException throw Exception : {}", notFoundException.getErrorCode());
        return ErrorResponse.toResponseEntity(notFoundException.getErrorCode(),request);
    }

    //409
    @ExceptionHandler(value = ConflictException.class)
    protected ResponseEntity<ErrorResponse> handleConflictException(HttpServletRequest request, ConflictException conflictException) {
        log.error("ErrorExceptionURI : " + request.getRequestURI());
        log.error("handleConflictException throw Exception : {}", conflictException.getErrorCode());
        return ErrorResponse.toResponseEntity(conflictException.getErrorCode(),request);
    }

    //415
    @ExceptionHandler(value = ConflictException.class)
    protected ResponseEntity<ErrorResponse> handleUnsupportedMediaTypeException(HttpServletRequest request, UnsupportedMediaTypeException unsupportedMediaTypeException) {
        log.error("ErrorExceptionURI : " + request.getRequestURI());
        log.error("handleUnsupportedMediaTypeException throw Exception : {}", unsupportedMediaTypeException.getErrorCode());
        return ErrorResponse.toResponseEntity(unsupportedMediaTypeException.getErrorCode(),request);
    }

    //500
    @ExceptionHandler( value = InternalServerException.class)
    protected ResponseEntity<ErrorResponse> handleInternalServerException(HttpServletRequest request,InternalServerException internalServerException) {
        log.error("ErrorExceptionURI : " + request.getRequestURI());
        log.error("handleInternalServerException throw Exception : {}", internalServerException.getErrorCode());
        return ErrorResponse.toResponseEntity(internalServerException.getErrorCode(),request);
    }
    // unknown
    @ExceptionHandler( value = Exception.class)
    protected ResponseEntity<ErrorResponse> handleUnknownException(HttpServletRequest request,Exception e) {
        log.error("ErrorExceptionURI : " + request.getRequestURI());
        log.error("handleUnknownException throw Exception : {}", ErrorCode.UNKNOWN_ERROR);
        return ErrorResponse.toResponseEntity(ErrorCode.UNKNOWN_ERROR,request);
    }
}

