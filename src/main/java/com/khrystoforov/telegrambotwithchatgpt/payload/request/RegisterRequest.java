package com.khrystoforov.telegrambotwithchatgpt.payload.request;

import com.khrystoforov.telegrambotwithchatgpt.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    private String username;
    private String password;
    private Long chatId;
    private Role role;
}
