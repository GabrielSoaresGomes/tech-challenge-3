package com.postech.api_gateway.services;

import com.postech.api_gateway.configuration.RabbitMQConfig;
import com.postech.api_gateway.dtos.CancellationSchedulingDTO;
import com.postech.api_gateway.dtos.CreationSchedulingDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SchedulingPublisherService {

    private final RabbitTemplate rabbitTemplate;

    public SchedulingPublisherService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendNewScheduling(CreationSchedulingDTO creationSchedulingDTO) {
        log.info("Enviando mensagem de criação de agendamento: {}", creationSchedulingDTO);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_COMMAND_NAME,
                RabbitMQConfig.CREATE_SCHEDULING_ROUTING_KEY_V1,
                creationSchedulingDTO
        );
    }

    public void sendCancellationScheduling(CancellationSchedulingDTO cancellationSchedulingDTO) {
        log.info("Enviando mensagem de cancelamento de agendamento: {}", cancellationSchedulingDTO);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_COMMAND_NAME,
                RabbitMQConfig.CANCEL_SCHEDULING_ROUTING_KEY_V1,
                cancellationSchedulingDTO
        );
    }
}
