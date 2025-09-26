package com.postech.history.dto;

public record RegisterConsultationResponseDTO(
        Long consultationHistoryId,
        Long consultationId,
        String message
) {}