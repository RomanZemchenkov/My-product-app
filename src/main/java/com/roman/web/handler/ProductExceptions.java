package com.roman.web.handler;

import com.roman.service.exception.ProductDoesntExistException;
import com.roman.service.exception.ProductException;
import com.roman.web.response.ErrorResponse;
import com.roman.web.response.Response;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.List;

@RestControllerAdvice(basePackages = "com.roman.web.controller")
public class ProductExceptions {

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<Response> productExceptionHandler(ProductException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductDoesntExistException.class)
    public ResponseEntity<Response> productDoesntExistExceptionHandler(ProductDoesntExistException e) {
        ErrorResponse message = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }
}
