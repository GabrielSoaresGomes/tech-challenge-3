package com.postech.scheduling_service.utils;

import com.postech.scheduling_service.client.HistoryClient;
import com.postech.scheduling_service.client.UserClient;
import com.postech.scheduling_service.dto.UserDto;
import com.postech.scheduling_service.dto.history.RegisterConsultationDto;
import com.postech.scheduling_service.entity.Scheduling;
import com.postech.scheduling_service.enums.StatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HistoryRegister {

    private final UserClient userClient;
    private final HistoryClient historyClient;

    public void register(Scheduling schedule, StatusEnum status) {
        if (schedule == null) {
            log.warn("[HistoryRegistrar] schedule nulo, ignorando registro");
            return;
        }

        Long patientId = schedule.getPatientId();
        Long doctorId  = schedule.getDoctorId();

        UserDto patient = FeignSafeUtils.safeCall(() -> userClient.getUser(patientId), "Paciente", patientId);
        UserDto doctor  = FeignSafeUtils.safeCall(() -> userClient.getUser(doctorId),  "Doutor",  doctorId);

        String patientName = patient != null ? patient.name() : "Paciente#" + patientId;
        String doctorName  = doctor  != null ? doctor.name()  : "Doutor#"   + doctorId;

        var dto = new RegisterConsultationDto(
                schedule.getId(),
                patientName,
                doctorName,
                schedule.getStartAt(),
                status
        );

        try {
            var resp = historyClient.registerConsultation(dto);
            log.info("[History] registrado. schedulingId={}, historyId={}, msg={}",
                    schedule.getId(),
                    resp.consultationHistoryId(),
                    resp.message());
        } catch (Exception e) {
            log.error("[History] falha ao registrar histórico: schedulingId={}, status={}",
                    schedule.getId(), status, e);
        }
    }

    public void registerCreated(Scheduling schedule)   { register(schedule, StatusEnum.SCHEDULED); }
    public void registerCancelled(Scheduling schedule) { register(schedule, StatusEnum.CANCELLED); }
    public void registerCompleted(Scheduling schedule) { register(schedule, StatusEnum.COMPLETED); }
    public void registerNoShow(Scheduling schedule)    { register(schedule, StatusEnum.NO_SHOW); }
    public void registerRescheduled(Scheduling schedule){ register(schedule, StatusEnum.RESCHEDULED); }
}
