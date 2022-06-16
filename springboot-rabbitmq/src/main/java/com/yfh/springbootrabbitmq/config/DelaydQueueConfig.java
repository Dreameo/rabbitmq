package com.yfh.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 延迟插件 配置类
 */
@Configuration
public class DelaydQueueConfig {

    // 延迟交换机
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    // 延迟队列
    public static final String DELAYED_QUEUE = "delayed.queue";

    // routtingkey

    public static final String DELAYED_ROUTTING_KEY = "delayed.routtingkey";

    @Bean
    public Queue delayedQueue() {
        return QueueBuilder.durable(DELAYED_QUEUE).build();

    }

    // 声明交换机
    @Bean
    public CustomExchange delayedExchange() {

        /**
         * 1. 交换机名称
         * 2. 交换机类型
         * 3. 是否需要持久化
         * 4. 是否需要自动删除
         * 5. 其他参数（Map）
         */
        Map<String, Object> argments = new HashMap<>();
        argments.put("x-delayed-type", "direct"); // 类型就是direct类型 使用routtingkey进行匹配
        return new CustomExchange(DELAYED_EXCHANGE_NAME, "x-delayed-message", true, false, argments);
    }

    @Bean
    public Binding queueBindingExchange(@Qualifier("delayedQueue") Queue delayedQueue,
                                                                          @Qualifier("delayedExchange") CustomExchange delayedExchange) {
        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with(DELAYED_ROUTTING_KEY).noargs();//
    }

}
