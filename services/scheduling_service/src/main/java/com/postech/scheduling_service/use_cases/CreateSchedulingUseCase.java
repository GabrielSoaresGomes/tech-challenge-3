package com.postech.scheduling_service.use_cases;

import com.postech.scheduling_service.client.UserClient;
import com.postech.scheduling_service.dto.CreateSchedulingDto;
import com.postech.scheduling_service.dto.SchedulingDto;
import com.postech.scheduling_service.dto.SendNotificationDto;
import com.postech.scheduling_service.entity.Scheduling;
import com.postech.scheduling_service.mapper.SchedulingMapper;
import com.postech.scheduling_service.provider.UserProvider;
import com.postech.scheduling_service.repository.SchedulingRepository;
import com.postech.scheduling_service.services.SchedulingPublisherService;
import com.postech.scheduling_service.use_cases.base.UseCase;
import com.postech.scheduling_service.utils.HistoryRegister;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
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
    private final UserClient userClient;

    @Override
    public SchedulingDto execute(CreateSchedulingDto params) {
        Long patientId = params.getPatientId();
        Long doctorId  = params.getDoctorId();
        Scheduling scheduling = mapper.toEntity(params);

        validateUserExists(patientId, "PATIENT");
        validateUserExists(doctorId, "DOCTOR");

        log.info("Inserindo agendamento no banco de dados: {}", scheduling);

        Scheduling saved = repository.save(scheduling);

        log.info("Agendamento salvo com sucesso: {}", saved);

        historyRegister.registerCreated(saved);

        var notificationDto = this.toSendNotificationDto(params);
        this.publish(notificationDto);
        return mapper.toDto(saved);
    }

    private SendNotificationDto toSendNotificationDto(CreateSchedulingDto source) {
        var doctor = this.userProvider.getUserById(source.getDoctorId());
        var patient = this.userProvider.getUserById(source.getPatientId());

        return new SendNotificationDto(
                doctor.name(),
                patient.name(),
                patient.email(),
                source.getStartAt(),
                source.getEndAt()
        );
    }

    private void validateUserExists(Long userId, String roleLabel) {
        try {
            var user = userClient.getUser(userId);

            if (!user.role().toString().equals(roleLabel)) {
                String msg = roleLabel + " com id " + userId + " não encontrado.";
                log.info("{} (role mismatch)", msg);
                throw new EntityNotFoundException(msg);
            }
        } catch (FeignException.NotFound error) {
            String msg = roleLabel + " com id " + userId + " não encontrado.";
            log.info("{} (404 do user-service)", msg);
            throw new EntityNotFoundException(msg);
        } catch (FeignException error) {
            String msg = "Falha ao consultar " + roleLabel.toLowerCase() + " (id " + userId + ") no serviço de usuários.";
            log.warn("{} Status remoto: {}", msg, error.status());
            throw new RuntimeException(msg);
        }
    }

    private void publish(SendNotificationDto dto) {
        this.publisherService.notifySchedulingCreated(dto);
    }
}
