package com.roman.web.response;

import lombok.Getter;

@Getter
public abstract class Response {

    private final boolean result;

    public Response(boolean result) {
        this.result = result;
    }
}
