package com.postech.scheduling_service.dtos;

import java.time.LocalDateTime;

public record CreationSchedulingDTO(
        Number patientId,
        Number doctorId,
        LocalDateTime appointmentDateTime
) {
}
