package com.example.springjpa.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApiResponse<T>{
    private final int statusCode;
    private final T data;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul", shape= JsonFormat.Shape.STRING)
    private final LocalDateTime serverDateTime;

    public ApiResponse(int statusCode, T data){
        this.statusCode = statusCode;
        this.data = data;
        this.serverDateTime = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> ok(T data){
        return new ApiResponse<>(200, data);
    }

    public static <T> ApiResponse<T> fail(int statusCode, T errData){
        return new ApiResponse<>(statusCode, errData);
    }

}
