package com.postech.history.dto;

import org.springframework.graphql.data.method.annotation.SchemaMapping;

import java.time.OffsetDateTime;

@SchemaMapping("RegisterConsultationResponse")
public record RegisterConsultationDTO(
        Long consultationId,
        String patientName,
        String doctorName,
        OffsetDateTime consultationDate,
        ConsultationStatus status
) { }
