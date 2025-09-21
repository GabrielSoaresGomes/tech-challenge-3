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

@RequiredArgsConstructor
@Component
public class ListSchedulingUseCase implements UseCase<ListSchedulingUseCase.ListSchedulingParams, Page<SchedulingDto>> {

    private final SchedulingRepository repository;
    private final SchedulingMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<SchedulingDto> execute(ListSchedulingParams params) {
        Pageable pageable = PageRequest.of(
                Math.max(0, params.page()),
                Math.max(1, params.size()),
                params.sort() != null ? params.sort() : Sort.by(Sort.Direction.DESC, "startAt")
        );

        StatusEnum statusEnum = null;
        if (params.status() != null && !params.status().isBlank()) {
            statusEnum = StatusEnum.valueOf(params.status().toUpperCase());
        }

        Page<Scheduling> page = repository.findAllByFilters(
                params.patientId(), params.doctorId(), statusEnum,
                params.fromDate(), params.toDate(), pageable
        );

        return mapper.toDtoPage(page);
    }

    @Builder
    public record ListSchedulingParams(
            Long patientId,
            Long doctorId,
            String status,
            LocalDateTime fromDate,
            LocalDateTime toDate,
            int page,
            int size,
            Sort sort
    ) {}
}
