package com.yfh.springbootrabbitmq.consumer;

import com.yfh.springbootrabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConfirmConsumer {

    @RabbitListener(queues = {ConfirmConfig.CONFIRM_QUEUE_NAME})
    public void receiveMsg(Message message) {
        log.info("队列接受到消息：{}", new String(message.getBody()));
    }
}
