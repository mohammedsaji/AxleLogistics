package com.app.projectlogistics.ExceptionHandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.security.InvalidKeyException;

@ControllerAdvice
public class FilterExceptionHandler {

    @ExceptionHandler(InvalidKeyException.class)
    public ResponseEntity<String> handleInvalidKeyException(InvalidKeyException invalidKeyException){
        return ResponseEntity.status(401).body(invalidKeyException.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public  ResponseEntity<String> handleIllegalAccessException(AccessDeniedException accessDeniedException){
        return ResponseEntity.status(403).body(accessDeniedException.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleIOException(AuthenticationException authenticationException){
        return ResponseEntity.status(401).body(authenticationException.getMessage());
    }
}
