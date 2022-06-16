package com.yfh.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.Backoff;

/**
 * 配置类： 发布确认 高级
 *
 */
@Configuration
public class ConfirmConfig {

    // 交换机
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    // 队列
    public static final String CONFIRM_QUEUE_NAME  = "confirm.queue";

    // RouttingKey
    public static final String CONFIRM_ROUTING_KEY = "confirm.routtingkey";

    // 备份交换机
    public static final String BACKUP_EXCHANGE_NAME = "confirm.routtingkey";
    // 备份队列
    public static final String BACKUP_QUEUE_NAME = "confirm.routtingkey";
    // 报警队列
    public static final String WARNING_QUEUE_NAME = "confirm.routtingkey";



    @Bean
    public Queue confirmQueue() {
       return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    @Bean
    public DirectExchange confirmExchange() {


        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME).durable(true)
                .withArgument("alternate-exchange", BACKUP_EXCHANGE_NAME).build();
    }

    @Bean
    public Binding queueBindingConfirmExchange(@Qualifier("confirmQueue") Queue confirmQueue,
                                               @Qualifier("confirmExchange") DirectExchange confirmExchange) {
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(CONFIRM_ROUTING_KEY);
    }

    @Bean
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    @Bean
    public Queue backQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }

    @Bean
    public Queue warnQueue() {
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }

    @Bean
    public Binding backQueueBindingExchange(@Qualifier("backQueue") Queue backQueue,
                                            @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(backQueue).to(backupExchange);
    }

    @Bean
    public Binding warnQueueBindingExchange(@Qualifier("warnQueue") Queue warnQueue,
                                            @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(warnQueue).to(backupExchange);
    }




}
