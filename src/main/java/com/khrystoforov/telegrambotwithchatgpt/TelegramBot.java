package com.khrystoforov.telegrambotwithchatgpt;

import com.khrystoforov.telegrambotwithchatgpt.service.TelegramBotService;
import lombok.extern.slf4j.Slf4j;
import org.mvnsearch.chatgpt.model.completion.chat.ChatCompletionRequest;
import org.mvnsearch.chatgpt.model.completion.chat.ChatCompletionResponse;
import org.mvnsearch.chatgpt.spring.service.ChatGPTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${bot.username}")
    private String botUsername;

    private final ChatGPTService chatGPTService;

    private final TelegramBotService telegramBotService;


    @Autowired
    public TelegramBot(@Value("${bot.token}") String botToken,
                       ChatGPTService chatGPTService,
                       TelegramBotService telegramBotService) {
        super(botToken);
        this.chatGPTService = chatGPTService;
        this.telegramBotService = telegramBotService;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }


    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();

            if (update.getMessage().hasText()) {
                String text = message.getText();

                if (text.startsWith("/register")) {
                    sendTextMessage(chatId, telegramBotService.registerUserInTelegramBot(message));
                } else if (!telegramBotService.isUserExistsByChatId(chatId)) {
                    sendTextMessage(chatId, "To use the bot please register:\n" +
                            "/register username password");
                } else {
                    askGpt(chatId, text);
                }
            }
        }

    }

    private void askGpt(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        String gptResponse = chatGPTService.chat(ChatCompletionRequest.of(text))
                .map(ChatCompletionResponse::getReplyText)
                .block();
        System.out.println(gptResponse);
        try {
            sendMessage.setChatId(String.valueOf(chatId));
            sendMessage.setText("GPT answers:\n" + gptResponse);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendTextMessage(long chatId, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

}
