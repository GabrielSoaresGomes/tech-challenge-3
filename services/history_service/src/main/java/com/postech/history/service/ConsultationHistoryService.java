package com.postech.history.service;

import com.postech.history.dto.ConsultationHistoryDTO;
import com.postech.history.dto.RegisterConsultationDTO;
import com.postech.history.dto.RegisterConsultationResponseDTO;
import com.postech.history.repository.ConsultationHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultationHistoryService {

    private final ConsultationHistoryRepository repo;

    public ConsultationHistoryService(ConsultationHistoryRepository repo) {
        this.repo = repo;
    }

    public List<ConsultationHistoryDTO> listAll() {
        return repo.listAll();
    }

    public List<ConsultationHistoryDTO> byConsultationId(long id) {
        return repo.findByConsultationId(id);
    }

    public List<ConsultationHistoryDTO> byPatientName(String name) {
        return repo.findByPatientName(name);
    }

    public RegisterConsultationResponseDTO register(RegisterConsultationDTO input) {
        return repo.insert(input);
    }
}
