package com.khrystoforov.telegrambotwithchatgpt.service;

import com.khrystoforov.telegrambotwithchatgpt.exception.UserNotFoundException;
import com.khrystoforov.telegrambotwithchatgpt.mapper.RequestHistoryMapper;
import com.khrystoforov.telegrambotwithchatgpt.model.User;
import com.khrystoforov.telegrambotwithchatgpt.payload.dto.RequestHistoryDTO;
import com.khrystoforov.telegrambotwithchatgpt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final RequestHistoryMapper requestHistoryMapper;

    public boolean isUserExistsByUsername(String username) {
        log.info("Method \"UserService.isUserExistsByUsername()\" was called");
        return userRepository.existsUserByUsername(username);
    }

    public void createUser(User user) {
        log.info("Method \"UserService.createUser()\" was called");
        userRepository.persist(user);
    }

    public User findUserByUsername(String username) {
        log.info("Method \"UserService.findUserByUsername()\" was called");
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> {
                    log.error("User with username {} not found", username);
                    return new UserNotFoundException(String.format("User with username %s not found",
                            username));
                });
    }

    public User findUserByUserChatId(Long chatId) {
        log.info("Method \"UserService.findUserByUserChatId()\" was called");
        return userRepository.findUserByChatId(chatId)
                .orElseThrow(() -> {
                    log.error("User with username {} not found", chatId);
                    return new UserNotFoundException(String.format("User with chatId %d not found",
                            chatId));
                });
    }

    public void update(User user) {
        log.info("Method \"UserService.findUserByUserChatId()\" was called");
        userRepository.update(user);
    }

    public User getCurrentUser() {
        log.info("Method \"UserService.getCurrentUser()\" was called");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return findUserByUsername(currentPrincipalName);
    }

    public List<RequestHistoryDTO> getUserHistory() {
        log.info("Method \"UserService.getUserHistory()\" was called");
        return getCurrentUser().getHistories().stream()
                .map(requestHistoryMapper::toDto)
                .toList();
    }

    public void addToHistory(Long chatId, String question, String answer) {
        log.info("Method \"UserService.addToHistory()\" was called");
        User user = findUserByUserChatId(chatId);
        user.addRequestHistory(question, answer);
        update(user);
    }
}
