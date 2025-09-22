package com.postech.scheduling_service.use_cases;

import com.postech.scheduling_service.client.HistoryClient;
import com.postech.scheduling_service.client.UserClient;
import com.postech.scheduling_service.dto.CreateSchedulingDto;
import com.postech.scheduling_service.dto.SchedulingDto;
import com.postech.scheduling_service.dto.UserDto;
import com.postech.scheduling_service.dto.history.RegisterConsultationDto;
import com.postech.scheduling_service.entity.Scheduling;
import com.postech.scheduling_service.mapper.SchedulingMapper;
import com.postech.scheduling_service.repository.SchedulingRepository;
import com.postech.scheduling_service.use_cases.base.UseCase;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class CreateSchedulingUseCase implements UseCase<CreateSchedulingDto, SchedulingDto> {
    private final SchedulingRepository repository;
    private final SchedulingMapper mapper;
    private final UserClient userClient;
    private final HistoryClient historyClient;

    @Override
    public SchedulingDto execute(CreateSchedulingDto params) {
        Scheduling scheduling = mapper.toEntity(params);

        log.info("Inserindo agendamento no banco de dados: {}", scheduling);

        Scheduling saved = repository.save(scheduling);

        log.info("Agendamento salvo com sucesso: {}", saved);

        Long patientId = saved.getPatientId();
        Long doctorId  = saved.getDoctorId();

        var patient = safeGetUser(patientId, "Paciente");
        var doctor  = safeGetUser(doctorId,  "Doutor");

        var registerDto = new RegisterConsultationDto(
                saved.getId(),
                patient != null ? patient.name() : "Paciente#" + patientId,
                doctor  != null ? doctor.name()  : "Doutor#"   + doctorId,
                saved.getStartAt(),
                saved.getStatus()
        );

        log.info("Dados para histórico de consultas: {}", registerDto);

        try {
            var resp = historyClient.registerConsultation(registerDto);
            log.info("Histórico registrado: {}", resp);
        } catch (Exception e) {
            log.error("Falha ao registrar histórico via GraphQL", e);
        }

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
