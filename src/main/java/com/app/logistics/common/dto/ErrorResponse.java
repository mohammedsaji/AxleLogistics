package com.app.logistics.common.dto;

import java.time.LocalDateTime;

public class ErrorResponse {

    private final LocalDateTime timeStamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;

    private ErrorResponse(Builder builder){
        this.timeStamp = builder.timeStamp;
        this.status = builder.status;
        this.error = builder.error;
        this.message = builder.message;
        this.path = builder.path;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public int getStatus() {
        return status;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public static class Builder{

        private LocalDateTime timeStamp;
        private final int status;
        private final String error;
        private String message;
        private String path;

        public Builder(int status,String error){
            this.status = status;
            this.error = error;
        }

        public Builder timeStamp(LocalDateTime timeStamp){
            this.timeStamp = timeStamp;
            return this;
        }

        public Builder message(String message){
            this.message = message;
            return this;
        }

        public Builder path(String path){
            this.path = path;
            return this;
        }

        public ErrorResponse build(){
            return new ErrorResponse(this);
        }
    }
}
