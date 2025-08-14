package com.postech.api_gateway.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_COMMAND_NAME = "app_command_exchange";
    public static final String CREATE_SCHEDULING_QUEUE = "create_scheduling_queue";
    public static final String CANCEL_SCHEDULING_QUEUE = "cancel_scheduling_queue";
    public static final String CREATE_SCHEDULING_ROUTING_KEY_V1 = "scheduling.create.v1";
    public static final String CANCEL_SCHEDULING_ROUTING_KEY_V1 = "scheduling.cancel.v1";

    public static final String EXCHANGE_EVENTS_NAME = "app_events_exchange";
    public static final String NOTIFY_SCHEDULING_CREATED_QUEUE   = "notify_scheduling_created_queue";
    public static final String NOTIFY_SCHEDULING_CANCELED_QUEUE  = "notify_scheduling_canceled_queue";
    public static final String SAVE_HISTORY_SCHEDULING_CREATED_QUEUE = "save_history_scheduling_created_queue";
    public static final String DELETE_HISTORY_SCHEDULING_CANCELED_QUEUE = "delete_history_scheduling_canceled_queue";
    public static final String APPOINTMENT_SCHEDULED_ROUTING_KEY_V1 = "appointment.scheduled.v1";
    public static final String APPOINTMENT_CANCELED_ROUTING_KEY_V1  = "appointment.canceled.v1";

    // --- Commands (Direct) ---
    @Bean
    public DirectExchange commandExchange() {
        return new DirectExchange(EXCHANGE_COMMAND_NAME, true, false);
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

    // --- Events (Topic) ---
    @Bean
    public TopicExchange eventsExchange() {
        return new TopicExchange(EXCHANGE_EVENTS_NAME, true, false);
    }

    @Bean
    public Queue notifySchedulingCreatedQueue() {
        return QueueBuilder.durable(NOTIFY_SCHEDULING_CREATED_QUEUE).build();
    }

    @Bean
    public Queue notifySchedulingCanceledQueue() {
        return QueueBuilder.durable(NOTIFY_SCHEDULING_CANCELED_QUEUE).build();
    }

    @Bean
    public Queue saveHistorySchedulingCreatedQueue() {
        return QueueBuilder.durable(SAVE_HISTORY_SCHEDULING_CREATED_QUEUE).build();
    }

    @Bean
    public Queue deleteHistorySchedulingCanceledQueue() {
        return QueueBuilder.durable(DELETE_HISTORY_SCHEDULING_CANCELED_QUEUE).build();
    }

    @Bean
    public Binding notifySchedulingCreatedBinding(
            @Qualifier("notifySchedulingCreatedQueue") Queue queue,
            TopicExchange eventsExchange) {
        return BindingBuilder.bind(queue).to(eventsExchange).with(APPOINTMENT_SCHEDULED_ROUTING_KEY_V1);
    }

    @Bean
    public Binding notifySchedulingCanceledBinding(
            @Qualifier("notifySchedulingCanceledQueue") Queue queue,
            TopicExchange eventsExchange) {
        return BindingBuilder.bind(queue).to(eventsExchange).with(APPOINTMENT_CANCELED_ROUTING_KEY_V1);
    }

    @Bean
    public Binding saveHistorySchedulingCreatedBinding(
            @Qualifier("saveHistorySchedulingCreatedQueue") Queue queue,
            TopicExchange eventsExchange) {
        return BindingBuilder.bind(queue).to(eventsExchange).with(APPOINTMENT_SCHEDULED_ROUTING_KEY_V1);
    }

    @Bean
    public Binding deleteHistorySchedulingCanceledBinding(
            @Qualifier("deleteHistorySchedulingCanceledQueue") Queue queue,
            TopicExchange eventsExchange) {
        return BindingBuilder.bind(queue).to(eventsExchange).with(APPOINTMENT_CANCELED_ROUTING_KEY_V1);
    }

    // --- Template/Converter ---
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        rabbitTemplate.setMandatory(true); // habilita returns
        return rabbitTemplate;
    }
}
