package com.khrystoforov.telegrambotwithchatgpt;

import io.hypersistence.utils.spring.repository.BaseJpaRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(
        value = "com.khrystoforov.telegrambotwithchatgpt.repository",
        repositoryBaseClass = BaseJpaRepositoryImpl.class
)
public class TelegramBotWithChatGptApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelegramBotWithChatGptApplication.class, args);
    }
}
