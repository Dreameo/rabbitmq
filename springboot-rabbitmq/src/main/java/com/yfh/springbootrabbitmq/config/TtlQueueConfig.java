package com.yfh.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * TTL队列配置文件类代码
 */

@Configuration
public class TtlQueueConfig {

    // 两大交换机 三个队列

    // 普通交换机名称
    public static final String X_EXCHANGE = "X";
    // 死信队列交换机名称
    public static final String Y_DEAD_EXCHANGE = "Y";

    // 两个普通队列的名称， 延迟时间分别为10s 和 40s
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    public static final String QUEUE_C = "QC";


    // 死信队列的名称

    public static final String DEAD_QUEUE_D = "QD";

    // 声明xExchange交换机
    @Bean("xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(X_EXCHANGE);
    }

    // 声明y Exchange 交换机
    @Bean("yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(Y_DEAD_EXCHANGE);
    }

    // 声明队列
    @Bean("queueA")
    public Queue queueA() {
        // 不需要new 队列
        Map<String, Object> arguments = new HashMap<>();
        // 设置死信交换机
        arguments.put("x-dead-letter-exchange", Y_DEAD_EXCHANGE);
        // 设置routing Key
        arguments.put("x-dead-letter-routing-key", "YD");

        arguments.put("x-message-ttl", 10 * 1000); // 设置ttl 单位是ms

        return QueueBuilder.durable(QUEUE_A).withArguments(arguments).build();
    }

    // 声明队列
    @Bean("queueB")
    public Queue queueB() {
        // 不需要new 队列
        Map<String, Object> arguments = new HashMap<>();
        // 设置死信交换机
        arguments.put("x-dead-letter-exchange", Y_DEAD_EXCHANGE);
        // 设置routing Key
        arguments.put("x-dead-letter-routing-key", "YD");

        arguments.put("x-message-ttl", 40 * 1000); // 设置ttl 单位是ms

        return QueueBuilder.durable(QUEUE_B).withArguments(arguments).build();
    }

    // 声明队列C
    @Bean("queueC")
    public Queue queueC() {

        Map<String, Object> arguments = new HashMap<>();
        // 设置死信交换机
        arguments.put("x-dead-letter-exchange", Y_DEAD_EXCHANGE);
        // 设置routingKey
        arguments.put("x-dead-letter-routing-key", "YD");
        return QueueBuilder.durable(QUEUE_C).withArguments(arguments).build();
    }

    // 队列C绑定交换机X
    @Bean
    public Binding queueCbindingX(@Qualifier("queueC") Queue queueC,
                                  @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueC).to(xExchange).with("XC");
    }


    // 声明死信队列
    // 声明队列
    @Bean("queueD")
    public Queue queueD() {
        return QueueBuilder.durable(DEAD_QUEUE_D).build();
    }

    // 绑定关系

    @Bean
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA,
                                  @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }
    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB,
                                  @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }

    @Bean
    public Binding queueDBindingY(@Qualifier("queueD") Queue queueD,
                                  @Qualifier("yExchange") DirectExchange yExchange) {
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }




}
