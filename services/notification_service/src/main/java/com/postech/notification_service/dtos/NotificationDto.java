package com.postech.notification_service.dtos;

import java.time.LocalDateTime;

public record NotificationDto(
        String doctorName,
        String patientName,
        String patientEmail,
        LocalDateTime startAt,
        LocalDateTime endAt
) {
}
