package com.khrystoforov.telegrambotwithchatgpt.exceptionHandler;

import com.khrystoforov.telegrambotwithchatgpt.payload.response.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(annotations = Controller.class)
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse> handleException(Exception e) {
        APIResponse response = new APIResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
