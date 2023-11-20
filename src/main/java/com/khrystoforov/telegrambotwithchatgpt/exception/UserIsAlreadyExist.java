package com.khrystoforov.telegrambotwithchatgpt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserIsAlreadyExist extends RuntimeException {
    public UserIsAlreadyExist(String message) {
        super(message);
    }
}
