package com.postech.history.dto;

import java.time.OffsetDateTime;

public record ConsultationHistoryDTO(
        Long id,
        Long consultationId,
        String patientName,
        String doctorName,
        OffsetDateTime consultationDate,
        ConsultationStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}