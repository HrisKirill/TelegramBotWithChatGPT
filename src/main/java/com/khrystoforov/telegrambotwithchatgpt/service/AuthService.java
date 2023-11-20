package com.khrystoforov.telegrambotwithchatgpt.service;

import com.khrystoforov.telegrambotwithchatgpt.exception.UserIsAlreadyExist;
import com.khrystoforov.telegrambotwithchatgpt.model.User;
import com.khrystoforov.telegrambotwithchatgpt.payload.request.LoginRequest;
import com.khrystoforov.telegrambotwithchatgpt.payload.request.RegisterRequest;
import com.khrystoforov.telegrambotwithchatgpt.payload.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public MessageResponse register(RegisterRequest registerRequest) {
        if (userService.isUserExistsByUsername(registerRequest.getUsername())) {
            log.error(String.format("User with username %s is already exist",
                    registerRequest.getUsername()));

            throw new UserIsAlreadyExist(String.format("User with username %s is already exist",
                    registerRequest.getUsername()));
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .chatId(registerRequest.getChatId())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .registeredAt(LocalDateTime.now())
                .role(registerRequest.getRole())
                .build();

        userService.createUser(user);
        log.info("User with username {} is created successfully", user.getUsername());

        return new MessageResponse(String.format("User with username %s is registered successfully",
                user.getUsername()));
    }

    public MessageResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("logging with [{}]", authentication.getPrincipal());
        return new MessageResponse("User login successfully");
    }
}
