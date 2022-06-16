package com.yfh.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Producer
 * 生产者： 发消息
 *
 */
public class Producer {

    // 队列名称
    public static final String QUEUE_NAME = "hello"; // hello队列

    public static void main(String[] args) throws IOException, TimeoutException { // 发消息

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

        // 发送消息 需要获取连接的信道 进行发消息
        Channel channel = connection.createChannel(); // 获取信道

        // 先使用 默认交换机

        // 生命一个队列
        /**
         * 1. 队列名称
         * 2. 队列里面的消息 是否持久化（磁盘） 默认是存在内存中
         * 3. 该队列是否只供一个消费者消费  是否进行消息共享  true： 可以多个消费者消费 false 只能一个消费者消费
         * 4. 是否自动删除 最后一个消费者端连接以后 该队自动删除 true自动删除  false不自动删除
         * 5. 其他参数
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 发消息
        String message = "hello world, 第一次使用Queue";

        /**
         * 1. 发送到哪个交换机
         * 2. 路由的key值 是哪一个 本次是队列的名称
         * 3. 其他参数信息
         * 4. 消息体
         */

        channel.basicPublish("", QUEUE_NAME, null, message.getBytes()); // 发送二进制

        System.out.println("消息发送完毕！");

    }
}
