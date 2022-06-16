package com.yfh.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消息队列 工具类  生产者 和消费者 都要使用 创建连接工厂 连接 获取信道
 *
 */
public class RabbitUtils {

    // 得到一个连接的Channel
    public static Channel getChannel() throws IOException, TimeoutException {
        // 创建一个连接工厂

        ConnectionFactory factory = new ConnectionFactory();
        // 工厂IP 连接RabbitMQ的队列
        factory.setHost("192.168.132.4");

        // 用户名
        factory.setUsername("admin");

        // 密码
        factory.setPassword("123");

        // 创建连接
        Connection connection = factory.newConnection();

        // 获取一个连接的信道
        Channel channel = connection.createChannel();

        return channel;
    }
}
