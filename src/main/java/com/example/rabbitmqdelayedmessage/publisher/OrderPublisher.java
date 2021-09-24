package com.example.rabbitmqdelayedmessage.publisher;

import com.example.rabbitmqdelayedmessage.dto.OrderStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.rabbitmqdelayedmessage.config.MessagingConfig;
import com.example.rabbitmqdelayedmessage.dto.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderPublisher {

    private static Logger LOGGER = LoggerFactory.getLogger(OrderPublisher.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(Order order, String restaurantName){
        //Sending messages to a delayed queue
        try
        {
            order.setOrderId(UUID.randomUUID().toString());
            OrderStatus orderStatus = new OrderStatus(order, "PROCESS", "order placed succesfully in " + restaurantName);

            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(orderStatus);
            rabbitTemplate.convertAndSend(MessagingConfig.EXCHANGE,MessagingConfig.ROUTING_KEY, jsonString, new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    //Set the delay millisecond value for the message
                    message.getMessageProperties().setHeader("x-delay",MessagingConfig.DELAY_TIME);
                    return message;
                }
            });
        }catch (JsonProcessingException e){
            LOGGER.error("Error Convert ObjectMapper:{}",e.getMessage());
        }

        LOGGER.info("send delay message Order Id:{}",order.getOrderId());
    }
}
