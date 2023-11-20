package com.khrystoforov.telegrambotwithchatgpt.controller;

import com.khrystoforov.telegrambotwithchatgpt.payload.request.LoginRequest;
import com.khrystoforov.telegrambotwithchatgpt.payload.request.RegisterRequest;
import com.khrystoforov.telegrambotwithchatgpt.payload.response.MessageResponse;
import com.khrystoforov.telegrambotwithchatgpt.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;


    @PostMapping({"/register", "/sign-up"})
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponse register(@RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    @PostMapping({"/login", "sign-in"})
    @ResponseStatus(HttpStatus.OK)
    public MessageResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }
}
