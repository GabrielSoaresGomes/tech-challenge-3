package com.postech.scheduling_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record UpdateSchedulingDto(
        Long id,
        @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startAt,
        @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endAt,
        String status
) {
    public UpdateSchedulingDto withId(Long id) {
        return new UpdateSchedulingDto(id, this.startAt, this.endAt, this.status);
    }
}


