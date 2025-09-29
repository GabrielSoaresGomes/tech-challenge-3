package com.postech.scheduling_service.use_cases.history;

import com.postech.scheduling_service.client.HistoryClient;
import com.postech.scheduling_service.dto.history.ConsultationDto;
import com.postech.scheduling_service.use_cases.base.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetHistoryByPatientNameUseCase implements UseCase<String, List<ConsultationDto>> {
    private final HistoryClient historyClient;

    @Override public List<ConsultationDto> execute(String name) {
        return historyClient.getByPatientName(name);
    }
}