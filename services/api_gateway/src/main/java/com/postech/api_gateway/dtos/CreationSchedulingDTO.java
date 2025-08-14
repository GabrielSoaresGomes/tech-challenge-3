package com.postech.api_gateway.dtos;

import java.time.LocalDateTime;

public record CreationSchedulingDTO(
        Number patientId,
        Number doctorId,
        LocalDateTime appointmentDateTime
) {
}
