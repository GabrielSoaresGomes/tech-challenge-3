package com.postech.scheduling_service.services;
import com.postech.scheduling_service.configuration.RabbitMQConfig;
import com.postech.scheduling_service.dto.CreateSchedulingDto;
import com.postech.scheduling_service.dto.SendNotificationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SchedulingPublisherService {

    private final RabbitTemplate rabbitTemplate;

    public SchedulingPublisherService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void notifySchedulingCreated(SendNotificationDto sendNotificationDto) {
        log.info("Enviando E-mail de criação de agendamento: {}", sendNotificationDto);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_COMMAND_NAME,
                RabbitMQConfig.NOTIFY_SCHEDULING_CREATED_ROUTING_KEY_V1,
                sendNotificationDto
        );
    }

    public void notifySchedulingCanceled(SendNotificationDto sendNotificationDto) {
        log.info("Enviando E-mail de cancelamento de agendamento: {}", sendNotificationDto);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_COMMAND_NAME,
                RabbitMQConfig.NOTIFY_SCHEDULING_CANCELED_ROUTING_KEY_V1,
                sendNotificationDto
        );
    }
}
