package com.example.rabbitmqdelayedmessage.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MessagingConfig {

    public static final String QUEUE = "testing_queue";
    public static final String EXCHANGE = "testing_exchange";
    public static final String ROUTING_KEY = "testing_routingKey";
    public static final String DELAY_TIME = "5000";

    @Bean
    public Queue delayQueue() {
        return new Queue(QUEUE);
    }

    @Bean
    public CustomExchange  delayCustomExchange() {
        //Create a custom switch that can send delayed messages
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(EXCHANGE, "x-delayed-message",true, false,args);
    }

    @Bean
    public Binding delayBinding(CustomExchange delayCustomExchange,Queue delayQueue) {
        return BindingBuilder
                .bind(delayQueue)
                .to(delayCustomExchange)
                .with(ROUTING_KEY)
                .noargs();
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
