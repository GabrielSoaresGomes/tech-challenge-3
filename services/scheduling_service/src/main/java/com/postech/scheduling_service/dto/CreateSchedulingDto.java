package com.postech.scheduling_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateSchedulingDto(
        @NotNull Long patientId,
        @NotNull Long doctorId,
        @NotNull @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startAt,
        @NotNull @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endAt
) {
}