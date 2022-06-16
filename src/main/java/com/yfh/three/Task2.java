package com.yfh.three;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.yfh.utils.RabbitUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 *
 * 消息在手动应答 时 不丢失  ，放回队列中重新消费
 *
 */
public class Task2 {
    // 队列名称
    public static final String QUEUE_NAME = "ack_queue";

    // 消息手动应答 task
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitUtils.getChannel();
        // 开启发布确认

        channel.confirmSelect(); // 信道开启确认模式 (发一条确认测试？发一批确认测试？异步？)

        boolean durable = true; // 队列持久化
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.nextLine();
            // 消息持久化
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_BASIC, message.getBytes(StandardCharsets.UTF_8));

            System.out.println("成功发出消息：" + message);
        }
    }
}
