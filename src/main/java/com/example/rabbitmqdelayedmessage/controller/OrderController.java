package com.example.rabbitmqdelayedmessage.controller;

import com.example.rabbitmqdelayedmessage.dto.Order;
import com.example.rabbitmqdelayedmessage.publisher.OrderPublisher;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderPublisher orderPublisher;

    @PostMapping("/{restaurantName}")
    public String bookOrder(@RequestBody Order order, @PathVariable String restaurantName) {
        orderPublisher.sendMessage(order, restaurantName);
        return "Success !!";
    }
}
