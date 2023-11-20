package com.khrystoforov.telegrambotwithchatgpt.controller;

import com.khrystoforov.telegrambotwithchatgpt.payload.request.RegisterRequest;
import com.khrystoforov.telegrambotwithchatgpt.payload.response.MessageResponse;
import com.khrystoforov.telegrambotwithchatgpt.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/telegram-bot/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponse registerAdmin(@RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

}
