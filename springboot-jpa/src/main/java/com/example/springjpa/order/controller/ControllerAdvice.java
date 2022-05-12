package com.example.springjpa.order.controller;

import com.example.springjpa.order.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(value= NoSuchElementException.class)
    public ApiResponse<String> noSuchElementHandler(NoSuchElementException exception){
        return ApiResponse.fail(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    @ExceptionHandler(value= Exception.class)
    public ApiResponse<String> exceptionHandler(Exception exception){
        return ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
    }

}
