package com.postech.scheduling_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateSchedulingDto {
    @NotNull(message = "patientId é obrigatório")
    private Long patientId;
    @NotNull(message = "doctorId é obrigatório")
    private Long doctorId;
    @NotNull @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startAt;
    @NotNull @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endAt;
}