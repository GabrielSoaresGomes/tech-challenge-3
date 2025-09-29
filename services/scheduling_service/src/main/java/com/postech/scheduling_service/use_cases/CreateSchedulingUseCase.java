package com.postech.scheduling_service.use_cases;

import com.postech.scheduling_service.dto.CreateSchedulingDto;
import com.postech.scheduling_service.dto.SchedulingDto;
import com.postech.scheduling_service.dto.SendNotificationDto;
import com.postech.scheduling_service.dto.UserDto;
import com.postech.scheduling_service.entity.Scheduling;
import com.postech.scheduling_service.mapper.SchedulingMapper;
import com.postech.scheduling_service.provider.UserProvider;
import com.postech.scheduling_service.repository.SchedulingRepository;
import com.postech.scheduling_service.services.SchedulingPublisherService;
import com.postech.scheduling_service.use_cases.base.UseCase;
import com.postech.scheduling_service.utils.HistoryRegister;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class CreateSchedulingUseCase implements UseCase<CreateSchedulingDto, SchedulingDto> {
    private final SchedulingRepository repository;
    private final SchedulingMapper mapper;
    private final HistoryRegister historyRegister;
    private final UserProvider userProvider;
    private final SchedulingPublisherService publisherService;

    @Override
    public SchedulingDto execute(CreateSchedulingDto params) {
        Scheduling scheduling = mapper.toEntity(params);

        log.info("Inserindo agendamento no banco de dados: {}", scheduling);

        Scheduling saved = repository.save(scheduling);

        log.info("Agendamento salvo com sucesso: {}", saved);

        historyRegister.registerCreated(saved);

        var notificationDto = this.toSendNotificationDto(params);
        this.publish(notificationDto);
        return mapper.toDto(saved);
    }

    private UserDto safeGetUser(Long id, String roleLabel) {
        try {
            return userProvider.getUserById(id);
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

    private SendNotificationDto toSendNotificationDto(CreateSchedulingDto source) {
        var doctor = this.userProvider.getUserById(source.doctorId());
        var patient = this.userProvider.getUserById(source.patientId());

        return new SendNotificationDto(
                doctor.name(),
                patient.name(),
                patient.email(),
                source.startAt(),
                source.endAt()
        );
    }


    private void publish(SendNotificationDto dto) {
        this.publisherService.notifySchedulingCreated(dto);
    }
}
