package com.yfh.one;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Consumer消费者：消费消息
 * 接受消息
 *
 */
public class Consumer {

    public static final String QUEUE_NAME = "hello"; // 队列名称

    public static void main(String[] args) throws IOException, TimeoutException { // 接受消息

        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();

        // 主机
        factory.setHost("192.168.132.4");
        factory.setUsername("admin");
        factory.setPassword("123");

        // 连接
        Connection connection = factory.newConnection();

        // 获取信道
        Channel channel = connection.createChannel();

        /**
         * 消费者消费消息
         * 1. 要消费的队列
         * 2. 消费成功之后 是否要自动应答 true 代表自动应答 false 代表手动应答
         * 3. 消费者成功消费的回调
         * 4. 未成功消费的回调
         */

        // 声明 接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            // 消息有消息头 消息体
            // 我们只要消息 体
            System.out.println(new String(message.getBody()));
        };

        // 取消消息时的回调
        CancelCallback cancelCallback = (consumerTag -> {
            System.out.println("消费消费被中断了");
        });

        // 应答方式： 主动答 和 手动答
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);

    }
}
