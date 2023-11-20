package com.khrystoforov.telegrambotwithchatgpt.repository;

import com.khrystoforov.telegrambotwithchatgpt.model.User;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseJpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByChatId(Long chatId);

    boolean existsUserByUsername(String username);
}
