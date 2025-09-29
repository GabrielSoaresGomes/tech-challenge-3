package com.postech.scheduling_service.dto.history;

import java.time.LocalDateTime;

public record ConsultationDto(
        Long id,
        Long consultationId,
        String patientName,
        String doctorName,
        String consultationDate,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
