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
}
