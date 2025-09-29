package com.postech.scheduling_service.dto.history;

public record RegisterConsultationResponseDto(
        Long consultationHistoryId,
        Integer consultationId,
        String message
) {}