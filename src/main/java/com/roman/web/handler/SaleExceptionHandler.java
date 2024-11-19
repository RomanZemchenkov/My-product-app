package com.roman.web.handler;

import com.roman.service.exception.SaleException;
import com.roman.web.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.roman.web.controller")
public class SaleExceptionHandler {

    @ExceptionHandler({SaleException.class})
    public ResponseEntity<ErrorResponse> saleExceptionHandler(SaleException e){
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
