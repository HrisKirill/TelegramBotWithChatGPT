package com.khrystoforov.telegrambotwithchatgpt.controller;

import com.khrystoforov.telegrambotwithchatgpt.service.GPTService;
import com.khrystoforov.telegrambotwithchatgpt.service.TelegramBot;
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
    private final GPTService gptService;
    private final TelegramBot telegramBot;

    @GetMapping("/show-history")
    public List<RequestHistoryDTO> showHistory() {
        return userService.getUserHistory();
    }

    @PostMapping("/ask-bot")
    public MessageResponse askBot(@RequestParam(name = "question") String question) {
        Long chatId = userService.getCurrentUser().getChatId();
        MessageResponse messageResponse = gptService.askGpt(chatId, question);
        telegramBot.sendTextMessage(chatId, messageResponse.getMessage());
        return messageResponse;
    }
}
