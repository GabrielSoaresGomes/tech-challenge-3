package com.postech.scheduling_service.consumers;

import com.postech.scheduling_service.configuration.RabbitMQConfig;
import com.postech.scheduling_service.dtos.CancellationSchedulingDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class CancelSchedulingConsumer {
    @RabbitListener(queues = RabbitMQConfig.CANCEL_SCHEDULING_QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public void processCancelScheduling(CancellationSchedulingDTO message) {
        log.info("Processando cancelamento de um agendamento: {}", message);
    }
}
