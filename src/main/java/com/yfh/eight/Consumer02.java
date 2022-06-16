package com.yfh.eight;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.yfh.utils.RabbitUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 死信队列的 ：消费者2
 *
 */
public class Consumer02 {
    public static final String DEAD_EXCHANGE = "dead_exchange";

    public static final String DEAD_QUEUE = "dead_queue";


    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitUtils.getChannel();

        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("dead queue接受到的消息：" + new String(message.getBody()));
        };

        CancelCallback cancelCallback = consumerTag -> {};
        channel.basicConsume(DEAD_QUEUE, true, deliverCallback, cancelCallback);
    }
}
