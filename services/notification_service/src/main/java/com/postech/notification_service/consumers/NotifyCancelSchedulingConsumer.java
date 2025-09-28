package com.postech.notification_service.consumers;

import com.postech.notification_service.config.RabbitMQConfig;
import com.postech.notification_service.dtos.NotificationDto;
import com.postech.notification_service.services.EmailService;
import com.postech.notification_service.utils.FormatterUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class NotifyCancelSchedulingConsumer {
    private final EmailService emailService;

    @RabbitListener(queues = RabbitMQConfig.NOTIFY_SCHEDULING_CANCELED_QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public void processCancelScheduling(NotificationDto message) {
        log.info("Processando cancelamento de um agendamento: {}", message);

        var subject = this.buildSubject(message);
        var content = this.buildEmailContent(message);
        emailService.sendEmail(message.patientEmail(), subject, content);
    }

    private String buildSubject(NotificationDto message) {
        var startDate = FormatterUtils.formatDate(message.startAt());
        return String.format("Cancelamento de agendamento - %s", startDate);
    }

    private String buildEmailContent(NotificationDto message) {
        var startDate = FormatterUtils.formatDateTime(message.startAt());
        var endDate = FormatterUtils.formatDateTime(message.endAt());

        return String.format(
                "Prezado(a) %s, informamos que sua consulta com o(a) Dr(a). %s no dia e horário %s foi cancelada. " +
                "Agradecemos pela compreenção.", message.patientName(), message.doctorName(), startDate);
    }
}
