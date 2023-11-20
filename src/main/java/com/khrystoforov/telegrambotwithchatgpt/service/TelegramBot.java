package com.khrystoforov.telegrambotwithchatgpt.service;

import com.khrystoforov.telegrambotwithchatgpt.model.User;
import com.khrystoforov.telegrambotwithchatgpt.model.enums.Role;
import com.khrystoforov.telegrambotwithchatgpt.payload.request.RegisterRequest;
import com.khrystoforov.telegrambotwithchatgpt.payload.response.MessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${bot.username}")
    private String botUsername;
    private final AuthService authService;
    private final UserService userService;
    private final GPTService gptService;
    static final String YES_BUTTON = "YES_BUTTON";
    static final String NO_BUTTON = "NO_BUTTON";

    static final String ERROR_TEXT = "Error occurred: ";

    @Autowired
    public TelegramBot(@Value("${bot.token}") String botToken,
                       AuthService authService, UserService userService,
                       GPTService gptService) {
        super(botToken);
        this.authService = authService;
        this.userService = userService;
        this.gptService = gptService;
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
    @Transactional
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();

            if (update.getMessage().hasText()) {
                String text = message.getText();
                String username = message.getChat().getUserName();
                if (text.startsWith("/register")) {
                    registerForm(chatId);
                } else if (!userService.isUserExistsByUsername(username)) {
                    sendTextMessage(chatId, "To use the bot please /register");
                } else {
                    User user = userService.findUserByUsername(username);
                    if (user.getChatId() == null) {
                        user.setChatId(chatId);
                        userService.update(user);
                    }
                    sendTextMessage(chatId, gptService.askGpt(chatId, text).getMessage());
                }
            }
        } else if (update.hasCallbackQuery()) {
            Message message = update.getCallbackQuery().getMessage();
            Long chatId = message.getChatId();
            String callbackData = update.getCallbackQuery().getData();
            long messageId = message.getMessageId();

            if (callbackData.equals(YES_BUTTON)) {
                executeEditMessageText(registerUserInTelegramBot(message),
                        chatId, messageId);
            } else if (callbackData.equals(NO_BUTTON)) {
                String text = "Have a good day";
                executeEditMessageText(text, chatId, messageId);
            }
        }

    }

    public String registerUserInTelegramBot(Message message) {
        log.info("Method \"TelegramBot.registerUserInTelegramBot()\" was called");
        Long chatId = message.getChatId();
        String username = message.getChat().getUserName();
        String password = UUID.randomUUID().toString();

        RegisterRequest registerRequest = RegisterRequest.builder()
                .password(password)
                .chatId(chatId)
                .username(username)
                .role(Role.USER)
                .build();

        try {
            MessageResponse registeredUser = authService.register(registerRequest);
            return String.format(registeredUser.getMessage()
                            .concat("\nYour credentials:\n")
                            .concat("Username: %s\n")
                            .concat("Password: %s"),
                    username, password);
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    private void registerForm(long chatId) {
        log.info("Method \"TelegramBot.registerForm()\" was called");
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Do you really want to register?");

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        var yesButton = new InlineKeyboardButton();

        yesButton.setText("Yes");
        yesButton.setCallbackData(YES_BUTTON);

        var noButton = new InlineKeyboardButton();

        noButton.setText("No");
        noButton.setCallbackData(NO_BUTTON);

        rowInLine.add(yesButton);
        rowInLine.add(noButton);

        rowsInLine.add(rowInLine);

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);

        executeMessage(message);
    }


    private void executeEditMessageText(String text, long chatId, long messageId) {
        log.info("Method \"TelegramBot.executeEditMessageText()\" was called");
        EditMessageText message = EditMessageText.builder()
                .chatId(String.valueOf(chatId))
                .text(text)
                .messageId((int) messageId)
                .build();
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(ERROR_TEXT + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void sendTextMessage(long chatId, String text) {
        log.info("Method \"TelegramBot.sendTextMessage()\" was called");
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        executeMessage(message);
    }

    private void executeMessage(SendMessage message) {
        log.info("Method \"TelegramBot.executeMessage()\" was called");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(ERROR_TEXT + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
