package com.khrystoforov.telegrambotwithchatgpt.controller;

import com.khrystoforov.telegrambotwithchatgpt.TelegramBot;
import com.khrystoforov.telegrambotwithchatgpt.payload.dto.RequestHistoryDTO;
import com.khrystoforov.telegrambotwithchatgpt.payload.response.MessageResponse;
import com.khrystoforov.telegrambotwithchatgpt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/telegram-bot/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final TelegramBot telegramBotService;

    @GetMapping("/show-history")
    public List<RequestHistoryDTO> showHistory() {
        return userService.getUserHistory();
    }

    @PostMapping("/ask-bot")
    public MessageResponse askBot(@RequestParam(name = "question") String question) {
        return telegramBotService.askGpt(userService.getCurrentUser().getChatId(), question);
    }
}
