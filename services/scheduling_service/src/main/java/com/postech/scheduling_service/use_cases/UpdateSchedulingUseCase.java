package com.postech.scheduling_service.use_cases;

import com.postech.scheduling_service.dto.SchedulingDto;
import com.postech.scheduling_service.dto.UpdateSchedulingDto;
import com.postech.scheduling_service.enums.StatusEnum;
import com.postech.scheduling_service.mapper.SchedulingMapper;
import com.postech.scheduling_service.repository.SchedulingRepository;
import com.postech.scheduling_service.use_cases.base.UseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UpdateSchedulingUseCase implements UseCase<UpdateSchedulingDto, SchedulingDto> {

    private final SchedulingRepository repository;
    private final SchedulingMapper mapper;

    @Transactional
    @Override
    public SchedulingDto execute(UpdateSchedulingDto params) {
        var scheduling = this.repository.getReferenceById(params.getId());
        scheduling.setId(params.getId());
        scheduling.setStartAt(params.getStartAt());
        scheduling.setEndAt(params.getEndAt());
        scheduling.setStatus(params.getStatus());

        var savedScheduling = this.repository.save(scheduling);

        return mapper.toDto(savedScheduling);
    }
}

