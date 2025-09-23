package com.postech.scheduling_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record SchedulingDto(
        Long id,
        Long patientId,
        Long doctorId,
        @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startAt,
        @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endAt,
        String status,
        @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") LocalDateTime createdAt,
        @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") LocalDateTime updatedAt
) {}
