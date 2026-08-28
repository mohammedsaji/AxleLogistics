package com.app.logistics.common.dto;

import java.time.LocalDateTime;

public class ApiResponse<T> {

    private final boolean success;
    private final String message;
    private final T data;
    private final LocalDateTime timeStamp;

    private ApiResponse(Builder<T> builder){
        this.success = builder.success;
        this.message = builder.message;
        this.data = builder.data;
        this.timeStamp = builder.timeStamp;
    }

    public T getData() {
        return data;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public static class Builder<T>{

        private final boolean success;
        private String message;
        private final T data;
        private LocalDateTime timeStamp;

        public Builder(boolean success, T data){
            this.success =success;
            this.data = data;
        }

        public Builder<T> message(String message){
            this.message = message;
            return this;
        }

        public Builder<T> timeStamp(){
            this.timeStamp = LocalDateTime.now();
            return this;
        }

        public ApiResponse<T> build(){
            return new ApiResponse<>(this);
        }
    }
}
