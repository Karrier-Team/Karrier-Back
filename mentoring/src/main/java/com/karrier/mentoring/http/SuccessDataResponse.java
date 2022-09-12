package com.karrier.mentoring.http;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class SuccessDataResponse<T> extends BasicResponse {

    private int count;

    private int status;

    private T body;

    public SuccessDataResponse(T body) {
        this.body = body;
        this.status = HttpStatus.OK.value();
        if(body instanceof List) {
            this.count = ((List<?>)body).size();
        } else {
            this.count = 1;
        }
    }
}