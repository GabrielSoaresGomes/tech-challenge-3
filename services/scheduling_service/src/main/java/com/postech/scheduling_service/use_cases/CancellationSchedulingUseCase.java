package com.postech.scheduling_service.use_cases;

import com.postech.scheduling_service.dto.SchedulingDto;
import com.postech.scheduling_service.dto.SendNotificationDto;
import com.postech.scheduling_service.entity.Scheduling;
import com.postech.scheduling_service.provider.UserProvider;
import com.postech.scheduling_service.repository.SchedulingRepository;
import com.postech.scheduling_service.services.SchedulingPublisherService;
import com.postech.scheduling_service.use_cases.base.UseCase;
import com.postech.scheduling_service.utils.HistoryRegister;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
@Slf4j
public class CancellationSchedulingUseCase implements UseCase<Long, SchedulingDto> {

    private final SchedulingRepository repository;
    private final HistoryRegister historyRegister;
    private final UserProvider userProvider;
    private final SchedulingPublisherService publisherService;

    @Override
    @Transactional
    public SchedulingDto execute(Long schedulingId) {
        log.info("Cancelando agendamento id={}", schedulingId);
        var schedule = this.repository.getReferenceById(schedulingId);

        historyRegister.registerCancelled(schedule);

        this.repository.delete(schedule);
        log.info("Agendamento id={} cancelado com sucesso", schedulingId);

        var notificationDto  = this.toSendNotificationDto(schedule);
        this.publish(notificationDto);

        return null;
    }

    private SendNotificationDto toSendNotificationDto(Scheduling source) {
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

    private void publish(SendNotificationDto dto) {
        this.publisherService.notifySchedulingCanceled(dto);
    }
}
