package com.yfh.two;

import com.rabbitmq.client.Channel;
import com.yfh.utils.RabbitUtils;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 *
 * 发消息的 生产者
 * 发送大量消息
 */
public class Task01 {
    public static final String QUEUE_NAME = "hello";

    // 发送大量消息
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitUtils.getChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 从控制台 接受信息

        Scanner scanner = new Scanner(System.in);

        while(scanner.hasNext()) {
            String message = scanner.nextLine();
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("发送完成");
        }
    }
}
