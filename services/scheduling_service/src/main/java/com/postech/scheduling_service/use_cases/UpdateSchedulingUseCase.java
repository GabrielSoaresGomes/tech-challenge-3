package com.postech.scheduling_service.use_cases;

import com.postech.scheduling_service.dto.SchedulingDto;
import com.postech.scheduling_service.dto.UpdateSchedulingDto;
import com.postech.scheduling_service.entity.Scheduling;
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

    @Override
    @Transactional
    public SchedulingDto execute(UpdateSchedulingDto params) {
        Scheduling existing = repository.findById(params.id())
                .orElseThrow(() -> new IllegalArgumentException("Scheduling não encontrado"));

        mapper.updateEntity(params, existing);

        Scheduling updated = repository.save(existing);

        return mapper.toDto(updated);
    }
}

