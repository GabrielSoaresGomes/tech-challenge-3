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
public class NotifyCreateSchedulingConsumer {
    private final EmailService emailService;

    @RabbitListener(queues = RabbitMQConfig.NOTIFY_SCHEDULING_CREATED_QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public void processCreateScheduling(NotificationDto message) {
        log.info("Processando criação de um agendamento: {}", message);

        var subject = this.buildSubject(message);
        var content = this.buildEmailContent(message);
        emailService.sendEmail(message.patientEmail(), subject, content);
    }

    private String buildSubject(NotificationDto message) {
        var startDate = FormatterUtils.formatDate(message.startAt());
        return String.format("Confirmação de agendamento - %s", startDate);
    }

    private String buildEmailContent(NotificationDto message) {
        var startDate = FormatterUtils.formatDateTime(message.startAt());
        var endDate = FormatterUtils.formatDateTime(message.endAt());

        return String.format(
                "Prezado(a) %s, informamos que seu agendamento foi realizado " +
                "com sucesso. Sua consulta será com o(a) Dr(a). %s no dia e horário %s. Solicitamos que compareça com alguns minutos de antecedência. Em caso de necessidade " +
                "de cancelamento ou remarcação, entre em contato com nossa equipe. Agradecemos a confiança.", message.patientName(), message.doctorName(), startDate);
    }
}
