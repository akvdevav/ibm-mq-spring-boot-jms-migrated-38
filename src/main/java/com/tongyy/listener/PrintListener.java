package com.tongyy.listener;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PrintListener {

    @RabbitListener(queuesToDeclare = @Queue(name = "print-queue"))
    public void onMessage(String message) {
        System.out.println(message);
    }
}