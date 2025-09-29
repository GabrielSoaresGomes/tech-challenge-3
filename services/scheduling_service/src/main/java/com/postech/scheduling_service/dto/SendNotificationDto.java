package com.postech.scheduling_service.dto;

import java.time.LocalDateTime;

public record SendNotificationDto(
        String doctorName,
        String patientName,
        String patientEmail,
        LocalDateTime startAt,
        LocalDateTime endAt
) {
}
