package com.example.rabbitmqdelayedmessage.consumer;

import com.example.rabbitmqdelayedmessage.config.MessagingConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class User {

    private static Logger LOGGER = LoggerFactory.getLogger(User.class);

    @RabbitListener(queues = MessagingConfig.QUEUE)
    public void consumeMessageFromQueue(String orderStatus) {
        LOGGER.info("Message recieved from queue : " + orderStatus);
    }
}
