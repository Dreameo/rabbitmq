package com.yfh.springbootrabbitmq.consumer;

import com.yfh.springbootrabbitmq.config.DelaydQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 延迟插件 交换机  接受延迟消息
 */
@Slf4j
@Component
public class DelayedQueueConsumer {

    @RabbitListener(queues = {DelaydQueueConfig.DELAYED_QUEUE})
    public void receiveDelayQueueMessage(Message message) {
        String msg = new String(message.getBody());
        log.info("当前时间：{}, 收到延迟队列的消息:{}", new Date().toString(), msg);
    }
}
