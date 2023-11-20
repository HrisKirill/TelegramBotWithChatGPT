package com.khrystoforov.telegrambotwithchatgpt.mapper;

import com.khrystoforov.telegrambotwithchatgpt.model.RequestHistory;
import com.khrystoforov.telegrambotwithchatgpt.payload.dto.RequestHistoryDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RequestHistoryMapper {
    RequestHistoryDTO toDto(RequestHistory requestHistory);
}
