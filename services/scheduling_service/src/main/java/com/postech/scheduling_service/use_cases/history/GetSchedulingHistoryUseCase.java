package com.postech.scheduling_service.use_cases.history;

import com.postech.scheduling_service.client.HistoryClient;
import com.postech.scheduling_service.dto.history.ConsultationDto;
import com.postech.scheduling_service.repository.SchedulingRepository;
import com.postech.scheduling_service.use_cases.base.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class GetSchedulingHistoryUseCase implements UseCase<Long, List<ConsultationDto>> {
    private final HistoryClient historyClient;

    @Override
    public List<ConsultationDto> execute(Long schedulingId) {
        return historyClient.getByConsultationId(schedulingId);
    }

}
