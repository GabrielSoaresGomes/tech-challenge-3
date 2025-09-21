package com.postech.scheduling_service.mapper;

import com.postech.scheduling_service.dto.CreateSchedulingDto;
import com.postech.scheduling_service.dto.SchedulingDto;
import com.postech.scheduling_service.dto.UpdateSchedulingDto;
import com.postech.scheduling_service.entity.Scheduling;
import com.postech.scheduling_service.enums.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SchedulingMapper {

    public SchedulingDto toDto(Scheduling e) {
        if (e == null) return null;
        return new SchedulingDto(
                e.getId(),
                e.getPatientId(),
                e.getDoctorId(),
                e.getStartAt(),
                e.getEndAt(),
                e.getStatus() != null ? e.getStatus().name() : null,
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }

    public Page<SchedulingDto> toDtoPage(Page<Scheduling> page) {
        return page.map(this::toDto);
    }

    public List<SchedulingDto> toDtoList(List<Scheduling> list) {
        return list == null ? List.of() : list.stream().map(this::toDto).toList();
    }

    public Scheduling toEntity(CreateSchedulingDto dto) {
        if (dto == null) return null;
        var e = new Scheduling();
        e.setPatientId(dto.patientId());
        e.setDoctorId(dto.doctorId());
        e.setStartAt(dto.startAt());
        e.setEndAt(dto.endAt());
        e.setStatus(StatusEnum.SCHEDULED);
        return e;
    }

    public void updateEntity(UpdateSchedulingDto dto, Scheduling e) {
        if (dto == null || e == null) return;

        if (dto.startAt() != null) e.setStartAt(dto.startAt());
        if (dto.endAt() != null)   e.setEndAt(dto.endAt());

        if (dto.status() != null && !dto.status().isBlank()) {
            try {
                e.setStatus(StatusEnum.valueOf(dto.status().toUpperCase()));
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException(
                        "Status inválido: " + dto.status() +
                                ". Aceitos: SCHEDULED, COMPLETED, CANCELED, NO_SHOW, RESCHEDULED"
                );
            }
        }
    }
}
