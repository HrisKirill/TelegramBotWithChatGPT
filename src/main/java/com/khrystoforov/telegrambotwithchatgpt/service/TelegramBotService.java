package com.khrystoforov.telegrambotwithchatgpt.service;

import com.khrystoforov.telegrambotwithchatgpt.payload.request.RegisterRequest;
import com.khrystoforov.telegrambotwithchatgpt.payload.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
@RequiredArgsConstructor
public class TelegramBotService {
    private final AuthService authService;
    private final UserService userService;

    public String registerUserInTelegramBot(Message message) {
        String receivedText = message.getText();
        String[] parts = receivedText.split(" ");
        Long chatId = message.getChatId();
        if (parts.length == 3) {
            String username = parts[1];
            String password = parts[2];

            RegisterRequest registerRequest = RegisterRequest.builder()
                    .password(password)
                    .chatId(chatId)
                    .username(username)
                    .build();
            try {
                MessageResponse registeredUser = authService.register(registerRequest);
                return String.format(registeredUser.getMessage().concat("""

                         Your credentials:
                         Chat id: %d
                         Password: your_password
                         Username: your_username
                        """), chatId);
            } catch (RuntimeException e) {
                return e.getMessage();
            }

        } else {
            return "Invalid registration command. Example:\n" +
                    "/register username password";
        }
    }


    public boolean isUserExistsByChatId(Long chatId) {
        return userService.isUserExistByChatId(chatId);
    }
}
