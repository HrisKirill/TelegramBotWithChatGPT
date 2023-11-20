package com.khrystoforov.telegrambotwithchatgpt.service;

import com.khrystoforov.telegrambotwithchatgpt.exception.UserNotFoundException;
import com.khrystoforov.telegrambotwithchatgpt.model.User;
import com.khrystoforov.telegrambotwithchatgpt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;


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


    public boolean isUserExistByChatId(Long chatId) {
        log.info("Method \"UserService.isUserExistByChatId()\" was called");
        return userRepository.existsUserByChatId(chatId);
    }
}
