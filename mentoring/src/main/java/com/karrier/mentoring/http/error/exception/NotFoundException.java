package com.karrier.mentoring.http.error.exception;

import com.karrier.mentoring.http.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

//401
@Getter
@RequiredArgsConstructor
public class NotFoundException extends RuntimeException{
    private final ErrorCode errorCode;
}