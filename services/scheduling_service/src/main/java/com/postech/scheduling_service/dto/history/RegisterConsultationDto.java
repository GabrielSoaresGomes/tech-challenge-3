package com.postech.scheduling_service.dto.history;

import com.postech.scheduling_service.enums.StatusEnum;

import java.time.LocalDateTime;

public record RegisterConsultationDto(
        Long consultationId,
        String patientName,
        String doctorName,
        LocalDateTime consultationDate,
        StatusEnum status
) {
}
