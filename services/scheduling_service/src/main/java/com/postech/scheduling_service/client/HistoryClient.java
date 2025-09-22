// com.postech.scheduling_service.client.HistoryClient
package com.postech.scheduling_service.client;

import com.postech.scheduling_service.dto.history.RegisterConsultationDto;
import com.postech.scheduling_service.dto.history.RegisterConsultationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class HistoryClient {

    private final HttpGraphQlClient historyGraphQlClient;

    private static final String REGISTER_MUTATION = """
        mutation RegisterConsultation($consultation: RegisterConsultation!) {
          registerConsultation(consultation: $consultation) {
            consultationHistoryId
            consultationId
            message
          }
        }
        """;

    public RegisterConsultationResponseDto registerConsultation(RegisterConsultationDto dto) {
        // Seu schema pede String para consultationDate — envie ISO-8601 com offset
        String isoDate = dto.consultationDate()
                .atZone(ZoneId.of("America/Sao_Paulo"))
                .toOffsetDateTime()
                .toString();

        Map<String, Object> input = Map.of(
                "consultationId", dto.consultationId(),
                "patientName", dto.patientName(),
                "doctorName", dto.doctorName(),
                "consultationDate", isoDate,
                "status", dto.status().name() // nomes do enum devem casar com o GraphQL
        );

        return historyGraphQlClient
                .document(REGISTER_MUTATION)
                .variable("consultation", input)
                .retrieve("registerConsultation")
                .toEntity(RegisterConsultationResponseDto.class)
                .block(); // como seu fluxo é síncrono aqui
    }
}
