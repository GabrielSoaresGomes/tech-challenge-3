package com.postech.notification_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_COMMAND_NAME = "app_command_exchange";
    public static final String EXCHANGE_EVENTS_NAME  = "app_events_exchange";

    public static final String CREATE_SCHEDULING_QUEUE = "create_scheduling_queue";
    public static final String CANCEL_SCHEDULING_QUEUE = "cancel_scheduling_queue";

    public static final String NOTIFY_SCHEDULING_CREATED_QUEUE   = "notify_scheduling_created_queue";
    public static final String NOTIFY_SCHEDULING_CANCELED_QUEUE  = "notify_scheduling_canceled_queue";

    // Consumer
    public static final String CREATE_SCHEDULING_ROUTING_KEY_V1 = "scheduling.create.v1";
    public static final String CANCEL_SCHEDULING_ROUTING_KEY_V1 = "scheduling.cancel.v1";

    // Producer
    public static final String APPOINTMENT_SCHEDULED_ROUTING_KEY_V1 = "appointment.scheduled.v1";
    public static final String APPOINTMENT_CANCELED_ROUTING_KEY_V1  = "appointment.canceled.v1";

    public static final String NOTIFY_SCHEDULING_CREATED_ROUTING_KEY_V1 = "notify.scheduling.create.v1";
    public static final String NOTIFY_SCHEDULING_CANCELED_ROUTING_KEY_V1 = "notify.scheduling.cancel.v1";

    @Bean
    public DirectExchange commandExchange() {
        return new DirectExchange(EXCHANGE_COMMAND_NAME, true, false);
    }

    @Bean TopicExchange eventsExchange()  {
        return new TopicExchange(EXCHANGE_EVENTS_NAME,  true, false);
    }


    @Bean
    public Queue createSchedulingQueue() {
        return QueueBuilder.durable(CREATE_SCHEDULING_QUEUE).build();
    }

    @Bean
    public Queue cancelSchedulingQueue() {
        return QueueBuilder.durable(CANCEL_SCHEDULING_QUEUE).build();
    }

    @Bean
    public Queue notifyCreateSchedulingQueue() {
        return QueueBuilder.durable(NOTIFY_SCHEDULING_CREATED_QUEUE).build();
    }

    @Bean
    public Queue notifyCancelSchedulingQueue() {
        return QueueBuilder.durable(NOTIFY_SCHEDULING_CANCELED_QUEUE).build();
    }

    @Bean
    public Binding createSchedulingBinding(
            @Qualifier("createSchedulingQueue") Queue queue,
            DirectExchange commandExchange) {
        return BindingBuilder.bind(queue).to(commandExchange).with(CREATE_SCHEDULING_ROUTING_KEY_V1);
    }

    @Bean
    public Binding cancelSchedulingBinding(
            @Qualifier("cancelSchedulingQueue") Queue queue,
            DirectExchange commandExchange) {
        return BindingBuilder.bind(queue).to(commandExchange).with(CANCEL_SCHEDULING_ROUTING_KEY_V1);
    }

    @Bean
    public Binding notifyCreateSchedulingBinding(
            @Qualifier("notifyCreateSchedulingQueue") Queue queue,
            DirectExchange commandExchange) {
        return BindingBuilder.bind(queue).to(commandExchange).with(NOTIFY_SCHEDULING_CREATED_ROUTING_KEY_V1);
    }

    @Bean
    public Binding notifyCancelSchedulingBinding(
            @Qualifier("notifyCancelSchedulingQueue") Queue queue,
            DirectExchange commandExchange) {
        return BindingBuilder.bind(queue).to(commandExchange).with(NOTIFY_SCHEDULING_CANCELED_ROUTING_KEY_V1);
    }

    @Bean
    public Jackson2JsonMessageConverter consumerMessageConverter(ObjectMapper mapper) {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(mapper);
        converter.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.INFERRED);
        return converter;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter consumerMessageConverter
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(consumerMessageConverter);
        return factory;
    }
}
