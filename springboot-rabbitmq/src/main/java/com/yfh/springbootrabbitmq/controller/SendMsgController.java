package com.yfh.springbootrabbitmq.controller;

import com.yfh.springbootrabbitmq.config.DelaydQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/ttl")
@Slf4j
public class SendMsgController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("sendMsg/{message}")
    public void sendMsg(@PathVariable String message) {
        log.info("当前时间{}, 发送一条消息给两个TTL队列{}", new Date(), message);
        rabbitTemplate.convertAndSend("X", "XA", "消息来自tt1为10s的队列" + message);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自tt2为40s的队列" + message);
    }

    @GetMapping("/sendMsg/{message}/{ttlTime}")
    public void sendMsg(@PathVariable String message,
                        @PathVariable String ttlTime) {
//        // 自定义设置 消息超时
        rabbitTemplate.convertAndSend("X", "XC", message, correlationData ->{
            correlationData.getMessageProperties().setExpiration(ttlTime);
            return correlationData;
        });
        log.info("当前时间：{},发送一条时长{}毫秒 TTL 信息给队列 C:{}", new Date(),ttlTime, message);
    }

    // 基于插件的 发送消息  及延迟消息

    @GetMapping("/sendDelayedMsg/{message}/{ttlTime}")
    public void sendDelayedMsg(@PathVariable String message,
                        @PathVariable String ttlTime) {
//        // 自定义设置 消息超时
//        rabbitTemplate.convertAndSend("X", "XC", message,  correlation -> {
//            correlation.getMessageProperties().setExpiration(ttlTime);
//            return correlation;
//        });

        rabbitTemplate.convertAndSend(DelaydQueueConfig.DELAYED_EXCHANGE_NAME, DelaydQueueConfig.DELAYED_ROUTTING_KEY, message, correlationData ->{
            correlationData.getMessageProperties().setDelay(Integer.valueOf(ttlTime)); // setDelay 设置延时
            return correlationData;
        });
        log.info("当前时间：{},发送一条时长{}毫秒 TTL 信息给队列 延迟队列DelayedQueue :{}", new Date(),ttlTime, message);
    }

}
