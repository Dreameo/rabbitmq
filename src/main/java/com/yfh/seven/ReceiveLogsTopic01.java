package com.yfh.seven;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.yfh.utils.RabbitUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *
 * Topic交换机
 * 消费者1
 *
 */
public class ReceiveLogsTopic01 {
    // 交换机名字
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitUtils.getChannel();

        // 声明队列
        String queueName = "Q1";

        channel.queueDeclare(queueName, false, false, false, null);

        // 绑定规则
        channel.queueBind(queueName, EXCHANGE_NAME, "*.orange.*");

        System.out.println("C1等待消息");

        DeliverCallback deliverCallback = ((consumerTag, message) -> {
            System.out.println("接收到的消息：" + new String(message.getBody()));
            System.out.println("接受到到队列：" + queueName + " 绑定的key：" + message.getEnvelope().getRoutingKey());
        });
    }
}
