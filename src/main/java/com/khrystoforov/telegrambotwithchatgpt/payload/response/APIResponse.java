package com.khrystoforov.telegrambotwithchatgpt.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class APIResponse {
    private String message;
    private HttpStatus status;
}
