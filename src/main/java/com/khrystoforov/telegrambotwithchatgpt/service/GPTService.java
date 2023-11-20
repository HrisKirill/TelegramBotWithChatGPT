package com.khrystoforov.telegrambotwithchatgpt.service;

import com.khrystoforov.telegrambotwithchatgpt.model.User;
import com.khrystoforov.telegrambotwithchatgpt.payload.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mvnsearch.chatgpt.model.completion.chat.ChatCompletionRequest;
import org.mvnsearch.chatgpt.model.completion.chat.ChatCompletionResponse;
import org.mvnsearch.chatgpt.spring.service.ChatGPTService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class GPTService {
    private final ChatGPTService chatGPTService;
    private final UserService userService;

    @Transactional
    public MessageResponse askGpt(Long chatId, String text) {
        log.info("Method \"GPTService.askGpt()\" was called");
        String gptResponse = chatGPTService.chat(ChatCompletionRequest.of(text))
                .map(ChatCompletionResponse::getReplyText)
                .block();

        userService.addToHistory(chatId, text, gptResponse);
        String answer = String.format("Question:\n%s \nAnswer:\n%s", text, gptResponse);

        return new MessageResponse(answer);
    }


}
