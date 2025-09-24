package com.postech.history.graphql;

import com.postech.history.dto.ConsultationHistoryDTO;
import com.postech.history.dto.RegisterConsultationDTO;
import com.postech.history.dto.RegisterConsultationResponseDTO;
import com.postech.history.service.ConsultationHistoryService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ConsultationHistoryResolver {

    private final ConsultationHistoryService service;

    public ConsultationHistoryResolver(ConsultationHistoryService service) {
        this.service = service;
    }

    @QueryMapping
    public List<ConsultationHistoryDTO> listConsultationHistory() {
        return service.listAll();
    }

    @QueryMapping
    public List<ConsultationHistoryDTO> getConsultationHistoryByConsultationId(@Argument int id) {
        return service.byConsultationId(id);
    }

    @QueryMapping
    public List<ConsultationHistoryDTO> getConsultationHistoryByPatientName(@Argument String name) {
        return service.byPatientName(name);
    }

    @MutationMapping
    public RegisterConsultationResponseDTO registerConsultation(@Argument("consultation") RegisterConsultationDTO input) {
        return service.register(input);
    }
}
