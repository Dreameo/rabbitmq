package com.yfh.two;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.yfh.utils.RabbitUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 一个工作线程： 相当于之前的 消费者 消费消息
 *
 */
public class Worker01 {

    // 队列的名称
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException { // 接收消息


        // 获取信道
        Channel channel = RabbitUtils.getChannel();

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("接受到的消息：" + new String(message.getBody()));
        };

        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消费者取消消费的回调逻辑!");
        };


        System.out.println("C1等待接受消息.........");
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);


    }
}
