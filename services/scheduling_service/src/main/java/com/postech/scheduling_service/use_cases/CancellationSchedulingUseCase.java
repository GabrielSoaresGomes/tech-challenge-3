package com.postech.scheduling_service.use_cases;

import com.postech.scheduling_service.dto.SchedulingDto;
import com.postech.scheduling_service.entity.Scheduling;
import com.postech.scheduling_service.enums.StatusEnum;
import com.postech.scheduling_service.mapper.SchedulingMapper;
import com.postech.scheduling_service.repository.SchedulingRepository;
import com.postech.scheduling_service.use_cases.base.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class CancellationSchedulingUseCase implements UseCase<Long, SchedulingDto> {

    private final SchedulingRepository repository;
    private final SchedulingMapper mapper;

    @Override
    @Transactional
    public SchedulingDto execute(Long schedulingId) {
        var schedule = this.repository.getReferenceById(schedulingId);

        this.repository.delete(schedule);

        return null;
    }
}
