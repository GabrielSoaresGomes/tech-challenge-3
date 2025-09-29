package com.postech.scheduling_service.client;

import com.postech.scheduling_service.dto.history.ConsultationDto;
import com.postech.scheduling_service.dto.history.RegisterConsultationDto;
import com.postech.scheduling_service.dto.history.RegisterConsultationResponseDto;
import com.postech.scheduling_service.graphql.GraphQlQueryLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class HistoryClient {

    private final HttpGraphQlClient historyGraphQlClient;
    private final GraphQlQueryLoader queries;

    public RegisterConsultationResponseDto registerConsultation(RegisterConsultationDto dto) {
        String isoDate = dto.consultationDate()
                .atZone(ZoneId.of("America/Sao_Paulo"))
                .toOffsetDateTime()
                .toString();

        Map<String, Object> input = Map.of(
                "consultationId", dto.consultationId(),
                "patientName", dto.patientName(),
                "doctorName", dto.doctorName(),
                "consultationDate", isoDate,
                "status", dto.status().name()
        );

        return historyGraphQlClient
                .document(queries.get("registerConsultation"))  // carrega registerConsultation.graphql
                .variable("consultation", input)
                .retrieve("registerConsultation")
                .toEntity(RegisterConsultationResponseDto.class)
                .block();
    }

    public List<ConsultationDto> getByConsultationId(Long consultationId) {
        return historyGraphQlClient
                .document(queries.get("getByConsultationId"))   // carrega getByConsultationId.graphql
                .variable("id", consultationId.intValue())
                .retrieve("getConsultationHistoryByConsultationId")
                .toEntityList(ConsultationDto.class)
                .block();
    }

    public List<ConsultationDto> listConsultationHistory() {
        return historyGraphQlClient
                .document(queries.get("listConsultationHistory"))
                .retrieve("listConsultationHistory")
                .toEntityList(ConsultationDto.class)
                .block();
    }

    public List<ConsultationDto> getByPatientName(String name) {
        return historyGraphQlClient
                .document(queries.get("getByPatientName"))
                .variable("name", name)
                .retrieve("getConsultationHistoryByPatientName")
                .toEntityList(ConsultationDto.class)
                .block();
    }
}
