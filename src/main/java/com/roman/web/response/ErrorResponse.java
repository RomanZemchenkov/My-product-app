package com.roman.web.response;

import lombok.Getter;

@Getter
public class ErrorResponse extends Response{

    private final String message;

    public ErrorResponse(String message) {
        super(false);
        this.message = message;
    }
}
