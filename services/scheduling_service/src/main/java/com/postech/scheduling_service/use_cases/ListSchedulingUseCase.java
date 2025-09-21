package com.postech.scheduling_service.use_cases;

import com.postech.scheduling_service.dto.SchedulingDto;
import com.postech.scheduling_service.entity.Scheduling;
import com.postech.scheduling_service.enums.StatusEnum;
import com.postech.scheduling_service.mapper.SchedulingMapper;
import com.postech.scheduling_service.repository.SchedulingRepository;
import com.postech.scheduling_service.use_cases.base.UseCase;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ListSchedulingUseCase implements UseCase<Void, List<SchedulingDto>> {

    private final SchedulingRepository repository;
    private final SchedulingMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<SchedulingDto> execute(Void params) {
        var schedules = this.repository.findAll();

        return schedules.stream()
                .map(mapper::toDto)
                .toList();
    }
}
