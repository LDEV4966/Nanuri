package com.example.nanuri.dto.http;

import lombok.Getter;

import java.util.List;

@Getter
public class SuccessDataResponse<T> extends BasicResponse {

    private int count;

    private int status;

    private T body;

    public SuccessDataResponse() {
        this.status = HttpStatusEnum.OK.statusCode;
    }

    public SuccessDataResponse(T body) {
        this.body = body;
        this.status = HttpStatusEnum.OK.statusCode;
        if(body instanceof List) {
            this.count = ((List<?>)body).size();
        } else {
            this.count = 1;
        }
    }
}
