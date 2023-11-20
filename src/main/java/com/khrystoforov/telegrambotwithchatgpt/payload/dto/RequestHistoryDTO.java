package com.khrystoforov.telegrambotwithchatgpt.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestHistoryDTO {
    private String question;
    private String answer;
    private LocalDateTime time;
}
