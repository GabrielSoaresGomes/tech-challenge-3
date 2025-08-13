package com.postech.api_gateway.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
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
    public static final String NOTIFY_SCHEDULING_CREATED_QUEUE = "notify_scheduling_created_queue";
    public static final String NOTIFY_SCHEDULING_CANCELED_QUEUE = "notify_scheduling_canceled_queue";
    public static final String SAVE_HISTORY_SCHEDULING_CREATED_QUEUE = "save_history_scheduling_created_queue";
    public static final String DELETE_HISTORY_SCHEDULING_CANCELED_QUEUE = "delete_history_scheduling_canceled_queue";
    public static final String APPOINTMENT_SCHEDULED_ROUTING_KEY_V1 = "appointment.scheduled.v1";
    public static final String APPOINTMENT_CANCELED_ROUTING_KEY_V1 = "appointment.canceled.v1";

    @Bean
    public DirectExchange commandExchange() {
        return new DirectExchange(EXCHANGE_COMMAND_NAME);
    }

    @Bean
    public Queue createSchedulingQueue() {
        return new Queue(CREATE_SCHEDULING_QUEUE, true);
    }

    @Bean
    public Queue cancelSchedulingQueue() {
        return new Queue(CANCEL_SCHEDULING_QUEUE, true);
    }

    @Bean
    public Binding createSchedulingBinding(Queue createSchedulingQueue, DirectExchange commandExchange) {
        return BindingBuilder.bind(createSchedulingQueue).to(commandExchange).with(CREATE_SCHEDULING_ROUTING_KEY_V1);
    }

    @Bean
    public Binding cancelSchedulingBinding(Queue cancelSchedulingQueue, DirectExchange commandExchange) {
        return BindingBuilder.bind(cancelSchedulingQueue).to(commandExchange).with(CANCEL_SCHEDULING_ROUTING_KEY_V1);
    }





    @Bean
    public DirectExchange eventsExchange() {
        return new DirectExchange(EXCHANGE_EVENTS_NAME);
    }

    @Bean
    public Queue notifySchedulingCreatedQueue() {
        return new Queue(NOTIFY_SCHEDULING_CREATED_QUEUE, true);
    }

    @Bean
    public Queue notifySchedulingCanceledQueue() {
        return new Queue(NOTIFY_SCHEDULING_CANCELED_QUEUE, true);
    }

    @Bean
    public Queue saveHistorySchedulingCreatedQueue() {
        return new Queue(SAVE_HISTORY_SCHEDULING_CREATED_QUEUE, true);
    }

    @Bean
    public Queue deleteHistorySchedulingCanceledQueue() {
        return new Queue(DELETE_HISTORY_SCHEDULING_CANCELED_QUEUE, true);
    }

    @Bean
    public Binding notifySchedulingCreatedBinding(Queue notifySchedulingCreatedQueue, DirectExchange eventsExchange) {
        return BindingBuilder.bind(notifySchedulingCreatedQueue).to(eventsExchange).with(APPOINTMENT_SCHEDULED_ROUTING_KEY_V1);
    }

    @Bean
    public Binding notifySchedulingCanceledBinding(Queue notifySchedulingCanceledQueue, DirectExchange eventsExchange) {
        return BindingBuilder.bind(notifySchedulingCanceledQueue).to(eventsExchange).with(APPOINTMENT_CANCELED_ROUTING_KEY_V1);
    }

    @Bean
    public Binding saveHistorySchedulingCreatedBinding(Queue saveHistorySchedulingCreatedQueue, DirectExchange eventsExchange) {
        return BindingBuilder.bind(saveHistorySchedulingCreatedQueue).to(eventsExchange).with(APPOINTMENT_SCHEDULED_ROUTING_KEY_V1);
    }

    @Bean
    public Binding deleteHistorySchedulingCanceledBinding(Queue deleteHistorySchedulingCanceledQueue, DirectExchange eventsExchange) {
        return BindingBuilder.bind(deleteHistorySchedulingCanceledQueue).to(eventsExchange).with(APPOINTMENT_CANCELED_ROUTING_KEY_V1);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

}
