package com.postech.scheduling_service.use_cases;

import com.postech.scheduling_service.client.HistoryClient;
import com.postech.scheduling_service.client.UserClient;
import com.postech.scheduling_service.dto.SchedulingDto;
import com.postech.scheduling_service.dto.history.RegisterConsultationDto;
import com.postech.scheduling_service.enums.StatusEnum;
import com.postech.scheduling_service.repository.SchedulingRepository;
import com.postech.scheduling_service.use_cases.base.UseCase;
import com.postech.scheduling_service.utils.FeignSafeUtils;
import com.postech.scheduling_service.utils.HistoryRegister;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
@Slf4j
public class CancellationSchedulingUseCase implements UseCase<Long, SchedulingDto> {

    private final SchedulingRepository repository;
    private final UserClient userClient;
    private final HistoryRegister historyRegister;

    @Override
    @Transactional
    public SchedulingDto execute(Long schedulingId) {
        log.info("Cancelando agendamento id={}", schedulingId);
        var schedule = this.repository.getReferenceById(schedulingId);

        historyRegister.registerCancelled(schedule);

        this.repository.delete(schedule);
        log.info("Agendamento id={} cancelado com sucesso", schedulingId);

        return null;
    }
}
