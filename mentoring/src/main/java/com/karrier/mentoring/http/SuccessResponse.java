package com.karrier.mentoring.http;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SuccessResponse extends BasicResponse {

    private int status;

    public SuccessResponse() {
        this.status = HttpStatus.OK.value();
    }
}

