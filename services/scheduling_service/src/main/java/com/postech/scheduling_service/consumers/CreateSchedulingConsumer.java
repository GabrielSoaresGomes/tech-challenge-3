package com.postech.scheduling_service.consumers;

import com.postech.scheduling_service.configuration.RabbitMQConfig;
import com.postech.scheduling_service.dto.CreateSchedulingDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class CreateSchedulingConsumer {
    @RabbitListener(queues = RabbitMQConfig.CREATE_SCHEDULING_QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public void processCreateScheduling(CreateSchedulingDto message) {
        log.info("Processando criação de um agendamento: {}", message);
    }
}
