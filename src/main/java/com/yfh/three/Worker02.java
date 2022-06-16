package com.yfh.three;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.yfh.utils.RabbitUtils;
import com.yfh.utils.SleepUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消息消费者 02 消费消息时间 长
 */
public class Worker02 {
    // 队列名称
    public static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitUtils.getChannel();

        System.out.println("C2等待接受的消息比较长！");


        DeliverCallback deliverCallback = ((consumerTag, message) -> {
            // 沉睡 时间

            SleepUtils.sleep(30); // 沉睡时间短

            System.out.println("接受到消息：" + new String(message.getBody(), "UTF-8"));

            // 消息处理完成 ，手动应答
            /**
             * 1. 消息的标记tag 每个消息有一个唯一标识
             * 2. 是否批量应答 false 不批量应答  true批量应答
             *
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);

        });

        // 设置不公平分发
//        int prefetchCount = 1;

        // 如果设置 prefetchCount > 1 那么表示就是预取值
        int prefetchCount = 5;
        channel.basicQos(prefetchCount);

        // 采用手动应答
        boolean autACK = false;
        channel.basicConsume(QUEUE_NAME, autACK, deliverCallback, ((consumerTag, message) -> System.out.println("取消消费消息的逻辑!")));
    }
}
