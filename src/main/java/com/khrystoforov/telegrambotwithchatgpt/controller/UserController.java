package com.khrystoforov.telegrambotwithchatgpt.controller;

import com.khrystoforov.telegrambotwithchatgpt.mapper.RequestHistoryMapper;
import com.khrystoforov.telegrambotwithchatgpt.payload.dto.RequestHistoryDTO;
import com.khrystoforov.telegrambotwithchatgpt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/telegram-bot/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/show-history")
    public List<RequestHistoryDTO> showHistory() {
        return userService.getUserHistory();
    }
}
