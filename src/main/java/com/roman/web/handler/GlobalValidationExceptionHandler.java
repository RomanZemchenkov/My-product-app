package com.roman.web.handler;

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
public class GlobalValidationExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Response> productValidationExceptionHandler(MethodArgumentNotValidException e) {
        StringBuilder sb = new StringBuilder();
        List<String> exceptionMessageList = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        for (String ex : exceptionMessageList) {
            if (!sb.isEmpty()) {
                sb.append("\n");
            }
            sb.append(ex);
        }

        return new ResponseEntity<>(new ErrorResponse(sb.toString()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({HandlerMethodValidationException.class})
    public ResponseEntity<Response> productMethodValidationExceptionHandler(HandlerMethodValidationException e) {
        StringBuilder sb = new StringBuilder();
        e.getAllValidationResults()
                .stream()
                .flatMap(el -> el.getResolvableErrors().stream().map(MessageSourceResolvable::getDefaultMessage))
                .forEach(message -> {
                    if(!sb.isEmpty()){
                        sb.append("\n");
                    }
                    sb.append(message);
                });
        System.out.println(" ");
        return new ResponseEntity<>(new ErrorResponse(sb.toString()), HttpStatus.BAD_REQUEST);
    }
}
