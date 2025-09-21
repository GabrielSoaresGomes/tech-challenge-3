package com.postech.scheduling_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.postech.scheduling_service.enums.StatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public final class UpdateSchedulingDto {
        private Long id;
        private @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startAt;
        private @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endAt;
        private StatusEnum status;
}


