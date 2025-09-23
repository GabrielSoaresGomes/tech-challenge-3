package com.postech.scheduling_service.use_cases;

import com.postech.scheduling_service.client.UserClient;
import com.postech.scheduling_service.dto.CreateSchedulingDto;
import com.postech.scheduling_service.dto.SchedulingDto;
import com.postech.scheduling_service.dto.UserDto;
import com.postech.scheduling_service.entity.Scheduling;
import com.postech.scheduling_service.enums.StatusEnum;
import com.postech.scheduling_service.mapper.SchedulingMapper;
import com.postech.scheduling_service.repository.SchedulingRepository;
import com.postech.scheduling_service.use_cases.base.UseCase;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
@Slf4j
public class CreateSchedulingUseCase implements UseCase<CreateSchedulingDto, SchedulingDto> {
    private final SchedulingRepository repository;
    private final SchedulingMapper mapper;
    private final UserClient userClient;

    @Override
    public SchedulingDto execute(CreateSchedulingDto params) {
        Scheduling scheduling = mapper.toEntity(params);

        log.info("Inserindo agendamento no banco de dados: {}", scheduling);

        Scheduling saved = repository.save(scheduling);

        log.info("Agendamento salvo com sucesso: {}", saved);

        Long patientId = scheduling.getPatientId();
        Long doctorId = scheduling.getDoctorId();

        UserDto patient = safeGetUser(patientId, "Paciente");
        UserDto doctor  = safeGetUser(doctorId,  "Doutor");

        Long schedulingId = saved.getId();
        StatusEnum schedulingStatus = scheduling.getStatus();
        LocalDateTime schedulingTime = scheduling.getStartAt();
        String patientName = patient != null ? patient.name() : "Paciente#" + patientId;
        String doctorName  = doctor  != null ? doctor.name()  : "Doutor#"   + doctorId;

        log.info("Scheduling created: id={}, status={}, time={}, patientId={}, patientName={}, doctorId={}, doctorName={}",
                schedulingId, schedulingStatus, schedulingTime, patientId, patientName, doctorId, doctorName);

        return mapper.toDto(saved);
    }

    private UserDto safeGetUser(Long id, String roleLabel) {
        try {
            return userClient.getUser(id);
        } catch (FeignException.Unauthorized e) {
            log.error("{} id={} não autorizado no auth-service (401).", roleLabel, id);
            return null;
        } catch (FeignException e) {
            log.error("Erro ao obter {} id={} no auth-service: status={}, body={}",
                    roleLabel, id, e.status(), safeBody(e));
            return null;
        } catch (Exception e) {
            log.error("Falha inesperada ao obter {} id={} no auth-service", roleLabel, id, e);
            return null;
        }
    }

    private String safeBody(FeignException e) {
        try {
            return e.contentUTF8();
        } catch (Exception ex) {
            return "<no-body>";
        }
    }
}
