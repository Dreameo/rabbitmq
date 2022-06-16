package com.yfh.five;


import com.rabbitmq.client.Channel;
import com.yfh.utils.RabbitUtils;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 消息生产者: 负责发消息 给交换机
 *
 */
public class EmitLog {

    // 交换机的名称
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitUtils.getChannel();

//        channel.exchangeDeclare(EXCHANGE_NAME, "fauout");

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String message = scanner.nextLine();

            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
            System.out.println("生产者发送消息：" + message);
        }
    }
}
