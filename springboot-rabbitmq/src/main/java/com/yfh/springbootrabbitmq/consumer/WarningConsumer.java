package com.yfh.springbootrabbitmq.consumer;

import com.yfh.springbootrabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WarningConsumer {

    @RabbitListener(queues = {ConfirmConfig.WARNING_QUEUE_NAME})
    public void receiveWarningMsg(Message message) {
        String msg = new String(message.getBody());
        log.error("报警发现不可路由的消息: {}", msg);
    }
}
