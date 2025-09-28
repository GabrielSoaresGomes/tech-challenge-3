package com.postech.scheduling_service.use_cases;

import com.postech.scheduling_service.dto.SchedulingDto;
import com.postech.scheduling_service.dto.SendNotificationDto;
import com.postech.scheduling_service.entity.Scheduling;
import com.postech.scheduling_service.provider.UserProvider;
import com.postech.scheduling_service.repository.SchedulingRepository;
import com.postech.scheduling_service.services.SchedulingPublisherService;
import com.postech.scheduling_service.use_cases.base.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class CancellationSchedulingUseCase implements UseCase<Long, SchedulingDto> {

    private final SchedulingRepository repository;
    private final UserProvider userProvider;
    private final SchedulingPublisherService publisherService;

    @Override
    @Transactional
    public SchedulingDto execute(Long schedulingId) {
        var schedule = this.repository.getReferenceById(schedulingId);

        this.repository.delete(schedule);

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
