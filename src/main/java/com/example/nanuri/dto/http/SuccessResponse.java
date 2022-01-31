package com.example.nanuri.dto.http;

import lombok.Getter;

@Getter
public class SuccessResponse extends BasicResponse {

    private int status;

    public SuccessResponse() {
        this.status = HttpStatusEnum.OK.statusCode;
    }
}
